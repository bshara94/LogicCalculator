package com.example.logiccalculator;


import android.util.Log;

public class FormalConverter {
    private char token;
    private String resultExpression;
    private final String theExpression;
    private int i = 0;

    public FormalConverter(String theExpression) {
        this.theExpression = theExpression;
        resultExpression = "";
    }

    private void nextToken() {
        if (i < theExpression.length()) {
            char ch = theExpression.charAt(i);
            if (Constants.isVoid(ch)) {
                return;
            } else if ( Constants.isVariable(ch) ) {
                token = ch;
            } else if ( Constants.isAND(ch) ) {
                token = Constants.AND;
            } else if ( Constants.isOR(ch) ) {
                token = Constants.OR;
            } else if ( Constants.isNOT(ch) ) {
                token = Constants.NOT;
            } else if ( Constants.isLEFT_PAR(ch) ) {
                token = Constants.LEFT_PAR;
            } else if ( Constants.isRIGHT_PAR(ch) ) {
                token = Constants.RIGHT_PAR;
            } else if ( Constants.isIMPLIES(ch) ) {
                token = Constants.IMPLIES;
            }
            i++;
        }
    }

    private TokenTreeNode procesaOperando() {
        TokenTreeNode t = null;
        if ( Constants.isVariable(token) ) {
            t = new TokenTreeNode(null,token, null);
            nextToken();
        } else if(token == Constants.NOT) {
            nextToken();
            t = new TokenTreeNode(Constants.NOT, procesaOperando());
        } else if (token == Constants.LEFT_PAR) {
            nextToken();
            t = startWFF();
            if (token == Constants.RIGHT_PAR) {
                nextToken();
            }
        }
        return t;
    }

    private TokenTreeNode startWFF() {
        return procesaOperador(Constants.IMPLIES);
    }

    /* Parse a sequence of one or more nonterminal(s) separated by value(s) */
    private TokenTreeNode procesaOperador(char operator) {
        TokenTreeNode t;
        if (operator == Constants.AND) {
            t = procesaOperando();
        } else if (operator == Constants.OR) {
            t = procesaOperador(Constants.AND);
        } else {
            t = procesaOperador(Constants.OR);
        }
        while ( token == operator ) {
            char tempActual = token;
            nextToken();
            if (operator == Constants.AND) {
                t = new TokenTreeNode(t, tempActual, procesaOperando());
            } else if (operator == Constants.OR) {
                t = new TokenTreeNode(t, tempActual, procesaOperador(Constants.AND));
            } else {
                t = new TokenTreeNode(t, tempActual, procesaOperador(Constants.OR));
            }
        }
        return t;
    }

    private TokenTreeNode removeImplies(TokenTreeNode t) {
        if (t.value == Constants.IMPLIES) {
            t = new TokenTreeNode(new TokenTreeNode(Constants.NOT, removeImplies(t.leftNode)),
                    Constants.OR,
                    removeImplies(t.rightNode));
        } else if (t.value == Constants.AND || t.value == Constants.OR) {
            t.leftNode  = removeImplies(t.leftNode);
            t.rightNode = removeImplies(t.rightNode);
        } else if (t.value == Constants.NOT) {
            t.negatedExpression = removeImplies(t.negatedExpression);
        }
        return t;
    }

    private TokenTreeNode pushNots(TokenTreeNode t) {
        if (t.value == Constants.NOT) {
            if (t.negatedExpression.value == Constants.AND) {
                t = new TokenTreeNode(pushNots(new TokenTreeNode(Constants.NOT, t.negatedExpression.leftNode)),
                        Constants.OR,
                        pushNots(new TokenTreeNode(Constants.NOT, t.negatedExpression.rightNode)));
            } else if (t.negatedExpression.value == Constants.OR) {
                t = new TokenTreeNode(pushNots(new TokenTreeNode(Constants.NOT, t.negatedExpression.leftNode)),
                        Constants.AND,
                        pushNots(new TokenTreeNode(Constants.NOT, t.negatedExpression.rightNode)));
            } else if (t.negatedExpression.value == Constants.NOT) {
                t = pushNots(t.negatedExpression.negatedExpression);
            }
        } else if (t.value == Constants.AND || t.value == Constants.OR) {
            t.leftNode  = pushNots(t.leftNode);
            t.rightNode = pushNots(t.rightNode);
        }
        return t;
    }

    private TokenTreeNode distribute(char op1, char op2, TokenTreeNode t) {
        if (t.value == op2) {
            t.leftNode = distribute(op1, op2, t.leftNode);
            t.rightNode= distribute(op1, op2, t.rightNode);
        } else if (t.value == op1) {
            TokenTreeNode p = distribute(op1, op2, t.leftNode);
            TokenTreeNode q = distribute(op1, op2, t.rightNode);
            if (p.value == op2) {
                t = new TokenTreeNode(distribute(op1, op2, new TokenTreeNode(p.leftNode, op1, q)),
                        op2,
                        distribute(op1, op2, new TokenTreeNode(p.rightNode,op1, q)));
            } else if(q.value == op2) {
                t = new TokenTreeNode(distribute(op1, op2, new TokenTreeNode(p, op1, q.leftNode)),
                        op2,
                        distribute(op1, op2, new TokenTreeNode(p, op1, q.rightNode)));
            } else {
                t.leftNode = p;
                t.rightNode = q;
            }
        }
        return t;
    }

    private TokenTreeNode convertToDNF(TokenTreeNode t) {
        return distribute(Constants.AND, Constants.OR, pushNots(removeImplies(t)));
    }

    private TokenTreeNode convertToCNF(TokenTreeNode t) {
        return distribute(Constants.OR, Constants.AND, pushNots(removeImplies(t)));
    }

    public void convert(String mode) {
        nextToken();
        TokenTreeNode wff = startWFF();
        Constants.println();

        Constants.println("Well formed formula: ");
        processConvert(wff);
        resultExpression = resultExpression.substring(0, resultExpression.length() );
        Constants.println(resultExpression);

        if (mode.equals("CNF")) {
            /* CNF */
            Constants.println();
            resultExpression = "";
            Constants.println("Conjunctive Normal Form: ");
            TokenTreeNode cnf = convertToCNF(wff);
            processConvert(cnf);
            resultExpression = resultExpression.substring(0, resultExpression.length() );
            Log.i("FORMALCONV","EXP IS:" + resultExpression );
            Constants.println(simplifyCNF(resultExpression));
            Constants.debugln("CNF Form:");
            BinaryTreePrinter.printNode(cnf);
        } else if (mode.equals("DNF")) {
            /* DNF */
            Constants.println();
            resultExpression = "";
            Constants.println("Disjunctive Normal Form: ");
            TokenTreeNode dnf = convertToDNF(wff);
            processConvert(dnf);
            resultExpression = resultExpression.substring(0, resultExpression.length() );
            Log.i("FORMALCONV","EXP DNF IS:" + resultExpression );
            Constants.print(simplifyDNF(resultExpression));
            Constants.debugln("DNF Form:");
            BinaryTreePrinter.printNode(dnf);
            //System.err.println("maxLevel:"+BinaryTreePrinter.maxLevel(wff));
        }
    }

    public String convertToCNF() {
        nextToken();
        TokenTreeNode wff = startWFF();
        processConvert(wff);
        resultExpression = resultExpression.substring(0, resultExpression.length());

        /* CNF */
        resultExpression = "";
        TokenTreeNode cnf = convertToCNF(wff);
        processConvert(cnf);
        resultExpression = resultExpression.substring(0, resultExpression.length());

        return resultExpression;
    }

    public String convertToDNF() {
        nextToken();
        TokenTreeNode wff = startWFF();
        processConvert(wff);
        resultExpression = resultExpression.substring(1, resultExpression.length()-1);

        /* DNF */
        resultExpression = "";
        TokenTreeNode dnf = convertToDNF(wff);
        processConvert(dnf);
        resultExpression = resultExpression.substring(1, resultExpression.length()-1);

        return resultExpression;
    }

    private void processConvert(TokenTreeNode t) {
        if (t != null) {
            if ( Constants.isVariable(t.value) ) {
                resultExpression += t.value;
            } else if ( Constants.isAND(t.value) || Constants.isOR(t.value) || Constants.isIMPLIES(t.value) ) {
                resultExpression += "(";
                processConvert(t.leftNode);
                String s;
                if ( Constants.isAND(t.value) ) {
                    s = " " + Constants.AND + " ";
                } else if ( Constants.isOR(t.value) ) {
                    s = " " + Constants.OR + " ";
                } else {
                    s = " " + Constants.IMPLIES + " ";
                }
                resultExpression += s;
                processConvert(t.rightNode);
                resultExpression += ")";
            } else if ( Constants.isNOT(t.value) ) {
                resultExpression += "" + Constants.NOT;
                processConvert(t.negatedExpression);
            }
        }
    }

    public static String simplifyCNF(String exp)
    {

        String res="",expr,temp,MinExp,ExpWithoutspace=exp;
        ExpWithoutspace=ExpWithoutspace.substring(1,ExpWithoutspace.length()-1);
        for(int i=0;i<ExpWithoutspace.length();i++)
        {
            if(ExpWithoutspace.charAt(i)==' ')
            {
                ExpWithoutspace=ExpWithoutspace.substring(0,i) + ExpWithoutspace.substring(i+1);
            }
        }
        System.out.println("No space:"+ExpWithoutspace);
        MinExp=ExpWithoutspace;
        expr=ExpWithoutspace.substring(1, ExpWithoutspace.length()-1);
        int c=countChar(expr,Constants.AND);///replace 'v' with the or symb
        for(int i=0;i<=c;i++)
        {
            if(i!=c)
            {
                int j=ExpWithoutspace.indexOf(Constants.AND);
                temp=ExpWithoutspace.substring(0,j);
                ExpWithoutspace=ExpWithoutspace.substring(j+1,ExpWithoutspace.length());

            }
            else
                temp=ExpWithoutspace;
            if(temp.length()>1)
            {
                //delete '(' ')'

                System.out.println("temp brfor remove ():"+temp);
                while((countChar(temp,'(')!=0)||(countChar(temp,')')!=0))
                    for(int l=0;l<temp.length();l++)
                    {
                        if(temp.charAt(l)=='(' || temp.charAt(l)==')')
                        {   if(l==0)
                        {
                            temp=temp.substring(1,temp.length());
                        }
                        else
                        {
                            if(l==temp.length()-1)
                                temp=temp.substring(0,temp.length()-1);
                            else
                                temp=temp.substring(0,l) + temp.substring(l+1);
                        }


                        }

                    }


                System.out.println("temp after remove ():"+temp);

                boolean b=true;
                for(int l=0;l<temp.length()-1;l++)
                {

                    if(temp.charAt(l)==Constants.NOT)
                    {
                        String CN=Constants.VISUAL_NOT+temp.charAt(l+1);
                        while(countString(temp,CN)!=1)
                        {
                            System.out.println("DELEt NOT:"+CN);
                            int t=temp.indexOf(CN);
                            if(t==0)
                            {
                                temp=temp.substring(t+3);//delete the atom and OR operrator
                            }
                            else
                            {
                                temp=temp.substring(0,t-1) + temp.substring(t+2);//delete the atom and OR operrator
                            }

                        }
                        b=false;
                    }

                    else
                    {
                        if((temp.charAt(l)!=Constants.OR)&&b)// diffrent of 'or' ///replace 'v' with the OR symbil
                        {

                            while(countChar(temp,temp.charAt(l))!=1)//if countChar>1 then we have somthin like this  r OR b ....OR r
                            {
                                int t=temp.indexOf(temp.charAt(l));
                                if( t<temp.length()-1)
                                {
                                    temp=temp.substring(0,t) + temp.substring(t+2);//delete the atom and OR operrator
                                }
                                else
                                {
                                    temp=temp.substring(0,t-1) + temp.substring(t+1);//delete the atom and OR operrator
                                }
                            }
                        }
                        if(temp.charAt(l)==Constants.OR) b=true;
                    }

                }
                /*kman for*/
                for(int l=0;l<temp.length();l++)
                {
                    if(temp.charAt(l)==Constants.NOT)
                    {
                        if(FindIfweHaveNot_NOtNOt(temp,temp.charAt(l+1))) //we have somthin like -r OR q OR -r  no need to add temp to the res
                        {
                            temp="";
                            break;
                        }
                    }
                }

                if((temp.length()<MinExp.length())&&(temp!=""))
                {
                    MinExp=temp;
                    System.out.println("MinExp:"+MinExp);

                    int Itemp=-1;
                    String[] parts = res.split("^");
                    if(parts.length==0) res="";
                    for(int l=0;l<parts.length;l++)
                    {
                        System.out.println("split "+parts[l]);
                        if(IsContain(parts[l],MinExp))
                        {
                            Itemp=res.indexOf(parts[l]);
                            System.out.println(parts[l]+" is con "+MinExp);
                        }
                    }
                    if(Itemp!=-1)
                    {

                        int FK=0,LK=0;
                        for(int l=Itemp;l>=0;l--)
                            if(res.charAt(l)=='(') {
                                FK=l;
                                break;
                            }
                        for(int l=Itemp;l<res.length();l++)
                            if(res.charAt(l)==')') {
                                LK=l;
                                break;
                            }
                        String Del="";
                        for(int ttt=FK;ttt<LK;ttt++)
                            Del=Del+res.charAt(ttt);
                        System.out.println("we deletete:"+Del+" MINEXPR:"+MinExp);
                        if((FK==0)&&(LK==res.length())) {res="";}
                        else
                        {
                            if(FK==0)
                            {
                                res=res.substring(LK+2,res.length());
                            }
                            if(LK==res.length()-1)
                            {
                                res=res.substring(0,FK-1);
                            }
                        }
                        if((FK>0)&&(LK<res.length()))
                        {
                            res=res.substring(0, FK-1)+res.substring(LK+1);
                        }

                    }
                    res=res+"("+temp+")"+Constants.AND;
                    System.out.println("res:"+res);
                }
                else
                {

                    if( (!IsContain(temp,MinExp))&&(temp!=""))
                        res=res+"("+temp+")"+Constants.AND;
                }


            }
            else
            {
                res=res+temp+Constants.AND;
            }

        }
        if(res!="")
            return res.substring(0, res.length()-1);//to delete the last '^'
        return exp;
    }



    public static String simplifyDNF(String exp)
    {
        String res="",expr,temp,MinExp,ExpWithoutspace=exp;
        ExpWithoutspace=ExpWithoutspace.substring(1,ExpWithoutspace.length()-1);
        for(int i=0;i<ExpWithoutspace.length();i++)
        {
            if(ExpWithoutspace.charAt(i)==' ')
            {
                ExpWithoutspace=ExpWithoutspace.substring(0,i) + ExpWithoutspace.substring(i+1);
            }
        }
        System.out.println("No space:"+ExpWithoutspace);
        MinExp=ExpWithoutspace;
        expr=ExpWithoutspace.substring(1, ExpWithoutspace.length()-1);
        int c=countChar(expr,Constants.OR);///replace 'v' with the or symb
        for(int i=0;i<=c;i++)
        {
            if(i!=c)
            {
                int j=ExpWithoutspace.indexOf(Constants.OR);
                temp=ExpWithoutspace.substring(0,j);
                ExpWithoutspace=ExpWithoutspace.substring(j+1,ExpWithoutspace.length());

            }
            else
                temp=ExpWithoutspace;
            if(temp.length()>1)
            {
                //delete '(' ')'

                System.out.println("temp brfor remove ():"+temp);
                while((countChar(temp,'(')!=0)||(countChar(temp,')')!=0))
                    for(int l=0;l<temp.length();l++)
                    {
                        if(temp.charAt(l)=='(' || temp.charAt(l)==')')
                        {   if(l==0)
                        {
                            temp=temp.substring(1,temp.length());
                        }
                        else
                        {
                            if(l==temp.length()-1)
                                temp=temp.substring(0,temp.length()-1);
                            else
                                temp=temp.substring(0,l) + temp.substring(l+1);
                        }


                        }

                    }


                System.out.println("temp after remove ():"+temp);

                boolean b=true;
                for(int l=0;l<temp.length()-1;l++)
                {

                    if(temp.charAt(l)==Constants.NOT)
                    {
                        String CN=Constants.VISUAL_NOT+temp.charAt(l+1);
                        while(countString(temp,CN)!=1)
                        {
                            System.out.println("DELEt NOT:"+CN);
                            int t=temp.indexOf(CN);
                            if(t==0)
                            {
                                temp=temp.substring(t+3);//delete the atom and OR operrator
                            }
                            else
                            {
                                temp=temp.substring(0,t-1) + temp.substring(t+2);//delete the atom and OR operrator
                            }

                        }
                        b=false;
                    }

                    else
                    {
                        if((temp.charAt(l)!=Constants.AND)&&b)// diffrent of 'or' ///replace 'v' with the OR symbil
                        {

                            while(countChar(temp,temp.charAt(l))!=1)//if countChar>1 then we have somthin like this  r OR b ....OR r
                            {
                                int t=temp.indexOf(temp.charAt(l));
                                if( t<temp.length()-1)
                                {
                                    temp=temp.substring(0,t) + temp.substring(t+2);//delete the atom and OR operrator
                                }
                                else
                                {
                                    temp=temp.substring(0,t-1) + temp.substring(t+1);//delete the atom and OR operrator
                                }
                            }
                        }
                        if(temp.charAt(l)==Constants.AND) b=true;
                    }

                }
                /*kman for*/
                for(int l=0;l<temp.length();l++)
                {
                    if(temp.charAt(l)==Constants.NOT)
                    {
                        if(FindIfweHaveNot_NOtNOt(temp,temp.charAt(l+1))) //we have somthin like -r or q or -r  no need to add temp to the res
                        {
                            temp="";
                            break;
                        }
                    }
                }

                if((temp.length()<MinExp.length())&&(temp!=""))
                {
                    MinExp=temp;
                    System.out.println("MinExp:"+MinExp);

                    int Itemp=-1;
                    String[] parts = res.split("v");
                    if(parts.length==0) res="";
                    for(int l=0;l<parts.length;l++)
                    {
                        System.out.println("split "+parts[l]);
                        if(IsContain(parts[l],MinExp))
                        {
                            Itemp=res.indexOf(parts[l]);
                            System.out.println(parts[l]+" is con "+MinExp);
                        }
                    }
                    if(Itemp!=-1)
                    {

                        int FK=0,LK=0;
                        for(int l=Itemp;l>=0;l--)
                            if(res.charAt(l)=='(') {
                                FK=l;
                                break;
                            }
                        for(int l=Itemp;l<res.length();l++)
                            if(res.charAt(l)==')') {
                                LK=l;
                                break;
                            }
                        String Del="";
                        for(int ttt=FK;ttt<LK;ttt++)
                            Del=Del+res.charAt(ttt);
                        System.out.println("we deletete:"+Del+" MINEXPR:"+MinExp);
                        if((FK==0)&&(LK==res.length())) {res="";}
                        else
                        {
                            if(FK==0)
                            {
                                res=res.substring(LK+2,res.length());
                            }
                            if(LK==res.length()-1)
                            {
                                res=res.substring(0,FK-1);
                            }
                        }
                        if((FK>0)&&(LK<res.length()))
                        {
                            res=res.substring(0, FK-1)+res.substring(LK+1);
                        }

                    }
                    res=res+"("+temp+")"+Constants.OR;
                    System.out.println("res:"+res);
                }
                else
                {

                    if( (!IsContain(temp,MinExp))&&(temp!=""))
                        res=res+"("+temp+")"+Constants.OR;
                }


            }
            else
            {
                res=res+temp+Constants.OR;
            }

        }
        if(res!="")
            return res.substring(0, res.length()-1);//to delete the last '^'
        return exp;
    }








    public static boolean FindIfweHaveNot_NOtNOt(String str, char c)
    {


        for(int i=0; i < str.length(); i++)
        {
            if(str.charAt(i) == c)//&& (str.charAt(i-1) != Constants.NOT))
            {
                if(i==0)
                    return true;
                if(str.charAt(i-1) != Constants.NOT)
                    return true;
            }
        }

        return false;
    }



    public static int countString(String str, String c)
    {
        int count = 0;

        for(int i=0; i < str.length()-1; i++)
        {    if((str.charAt(i) == c.charAt(0))&&(str.charAt(i+1) == c.charAt(1)))
            count++;
        }

        return count;
    }



    public static int countChar(String str, char c)
    {
        int count = 0;

        for(int i=0; i < str.length(); i++)
        {    if(str.charAt(i) == c)
        {
            if((i>0)&&(str.charAt(i-1)!=Constants.NOT))
                count++;
            if(i==0) count++;
        }
        }

        return count;
    }

    public static boolean IsContain(String str1,String str2)//str2-small
    {
        String res="";
        int [] lstr1=new int[26];
        int [] lstr2=new int[26];
        for(int i=0; i <26; i++)
        {
            lstr1[i]=0;
            lstr2[i]=0;
        }
        if(str1.length()==1)
        {
            int index=str1.charAt(0)-'a';
            lstr1[index]=1;
        }
        if(str2.length()==1)
        {
            int index=str2.charAt(0)-'a';
            lstr2[index]=1;
        }
        for(int i=0; i < str1.length()-1; i++)
        {
            if(str1.charAt(i)==Constants.NOT)
            {
                int index=str1.charAt(i+1)-'a';
                lstr1[index]=2;
                i++;

            }
            else
            {
                if((str1.charAt(i)!=Constants.AND)&&(str1.charAt(i)!=Constants.OR)&&(str1.charAt(i)!=')')&&(str1.charAt(i)!='('))
                {
                    int index=str1.charAt(i)-'a';
                    lstr1[index]=1;
                }
            }
        }

        for(int i=0; i < str2.length()-1; i++)
        {

            if(str2.charAt(i)==Constants.NOT)
            {
                int index=str2.charAt(i+1)-'a';
                lstr2[index]=2;
                i++;
            }
            else
            {

                if((str2.charAt(i)!=Constants.AND)&&(str2.charAt(i)!=Constants.OR)&&(str2.charAt(i)!=')')&&(str2.charAt(i)!='('))
                {
                    int index=str2.charAt(i)-'a';
                    lstr2[index]=1;

                }
            }
        }





        for(int i=0; i <26; i++)
        {
            if(lstr2[i]!=0)
            {
                if(lstr2[i]!=lstr1[i])
                    return false;
            }
        }
        return true;
    }



}
