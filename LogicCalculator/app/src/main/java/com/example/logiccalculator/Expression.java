package com.example.logiccalculator;


public class Expression {
    private String visualExpression;
    private String internalExpression;
    private String rpnExpression;

    public Expression() {
        this.visualExpression = new String();
        this.internalExpression  = new String();
        this.rpnExpression = new String();
    }

    /**
     * @return the visualExpression
     */
    public String getVisualExpression() {
        return visualExpression;
    }

    /**
     * @param visualExpression the visualExpression to set
     */
    public void setVisualExpression(String visualExpression) {
        this.visualExpression = visualExpression;
    }

    /**
     * @return the logicExpression
     */
    public String getInternalExpression() {
        return internalExpression;
    }

    /**
     * @param internalExpression the internalExpression to set
     */
    public void setInternalExpression(String internalExpression) {
        this.internalExpression = internalExpression;
    }

    /**
     * @return the rpnExpression
     */
    public String getRPNExpression() {
        return rpnExpression;
    }

    /**
     * @param rpnExpression the posfixExpression to set
     */
    public void setRPNExpression(String rpnExpression) {
        this.rpnExpression = rpnExpression;
    }

    public void clear() {
        this.visualExpression = new String();
        this.internalExpression  = new String();
        this.rpnExpression = new String();
    }

}
