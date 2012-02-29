/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation;

import edu.umiacs.irods.api.pi.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author toaster
 */
public class Rule
{

    private ActionDefinition actionDef;
    private Conditional conditional;
    private List<ActionChain> workflowChain = new ArrayList<ActionChain>();
    private List<ActionChain> recoveryChain = new ArrayList<ActionChain>();

    private Rule()
    {

    }

    /**
     * Parse a given string and return the corresponding rule
     * 
     * @param s rule string to parse
     * 
     * @return parsed rule
     */
    public static Rule parseRule(String s) throws ParseException
    {
        Rule r;

        String[] ar = s.split("\\|");
        if ( ar == null || ar.length != 4 )
        {
            throw new ParseException("Could not extract parts from rule: " + s);
        }

        r = new Rule();

        // parse 4 parts of rule, actiondef, conditional, workflow, recovery
        r.actionDef = r.parseActionDefinition(ar[0]);
        r.conditional = r.parseConditional(ar[1]);
        String[] workAr = ar[2].split("##");
        String[] recoverAr = ar[3].split("##");
//        if (workAr != null && recoverAr.length)
        for ( String workflow :workAr )
        {
            r.workflowChain.add(r.parseActionChain(workflow));
        }
        for ( String recovery : recoverAr )
        {
            r.recoveryChain.add(r.parseActionChain(recovery));
        }

        return r;

    }

    public Rule.ActionDefinition getActionDef()
    {
        return actionDef;
    }

    public Conditional getConditional()
    {
        return conditional;
    }

    public List<ActionChain> getWorkflowChain()
    {
        return workflowChain;
    }

    /**
     * Get the current recovery chain. If the length is 0, then a nop will
     * be printed when this rule is rendered
     * @return current recovery chain list
     */
    public List<ActionChain> getRecoveryChain()
    {
        return recoveryChain;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(actionDef);
        sb.append('|');
        sb.append(conditional);
        sb.append('|');
        if ( workflowChain.size() < 1 )
        {
            sb.append("nop");
        }
        else
        {
            for ( int i = 0; i < workflowChain.size(); i++ )
            {
                sb.append(workflowChain.get(i));
                if ( i != (workflowChain.size() - 1) )
                {
                    sb.append("##");
                }
            }
        }
        sb.append('|');
        if ( recoveryChain.size() < 1 )
        {
            sb.append("nop");
        }
        else
        {
            for ( int i = 0; i < recoveryChain.size(); i++ )
            {
                sb.append(recoveryChain.get(i));
                if ( i != (recoveryChain.size() - 1) )
                {
                    sb.append("##");
                }
            }
            // make sure # steps in recovery is same as number of steps in workflow
            for ( int i = recoveryChain.size(); i < workflowChain.size(); i++ )
            {
                sb.append("##nop");

            }
        }
//        return actionDef + "|" + conditional + "|" + workflowChain + "|" +
//                recoveryChain;

        return sb.toString();
    }

    /**
     * Parse a single workflow or recovery. The stuff between | and ##
     * 
     * @param s
     * @return
     * @throws edu.umiacs.irods.api.pi.ParseException
     */
    private ActionChain parseActionChain(String s) throws ParseException
    {
        ActionChain ac;
        String name, params;

        if ( s == null || s.length() < 1 )
        {
            return new ActionChain("nop");
        }

        // pattern for word(arg1,arg2..)
        // word may contain a-zA-Z0-1
        // arg may contain $*a-zA-Z0-9 and space
        // group 1 - function name, group 2 - function arguments
        Pattern p =
                Pattern.compile("(\\w+){1}(\\(([\\$|\\*|\\.| |\\w+]+\\w+,)*[\\$|\\*|\\.| |\\w+]+\\w++\\))?");

        Matcher m = p.matcher(s);
        if ( !m.matches() )
        {
            throw new ParseException("Cannot parse ActionDefinition: " + s);
        }

        // grab rule name
        name = m.group(1);
        ac = new ActionChain(name);
//        System.out.println("length " + m.groupCount() + " group 2 " + m.group(2));

        // if we have any arguments
        if ( m.group(2) != null )
        {
            params = m.group(2).substring(1, m.group(2).length() - 1);

            for ( String param : params.split(",") )
            {
                ac.getParameters().add(param);
            }
        }
        return ac;
    }

    private Conditional parseConditional(String s) throws ParseException
    {
        Conditional c = new Conditional();

        if ( s == null || s.length() < 1 )
        {
            return c;
        }

        return c;
    }

    private ActionDefinition parseActionDefinition(String s) throws ParseException
    {
        ActionDefinition ad;
        String name, params;


        Pattern p = Pattern.compile("(\\w+){1}(\\((\\w+,)*\\w+\\))?");

        Matcher m = p.matcher(s);
        if ( !m.matches() )
        {
            throw new ParseException("Cannot parse ActionDefinition: " + s);
        }

        // grab rule name
        name = m.group(1);
        ad = new ActionDefinition(name);
        // if we have any parameters
        if ( m.group(2) != null )
        {
            params = m.group(2).substring(1, m.group(2).length() - 1);

            for ( String param : params.split(",") )
            {
                ad.getParameters().add(param);
            }
        }
        return ad;
    }

    public class ActionChain extends ActionDefinition
    {

        private ActionChain(String name, String... parameters)
        {
            super(name, parameters);
        }
    }

    public class Conditional
    {

        @Override
        public String toString()
        {
            return "";
        }
    }

    public class ActionDefinition
    {

        private String name;
        private ArrayList<String> parameters;

        /**
         * Create a new action definition
         * @param name name of this action
         * @param parameters list of parameter names that this action takes
         */
        private ActionDefinition(String name, String... parameters)
        {
            this.name = name;
            if ( parameters != null && parameters.length > 0 )
            {
                this.parameters = new ArrayList<String>();
                for ( String s : parameters )
                {
                    this.parameters.add(s);
                }
            }
        }

        public List<String> getParameters()
        {
            if ( parameters == null )
            {
                parameters = new ArrayList<String>();
            }

            return parameters;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            if ( parameters != null && parameters.size() > 0 )
            {
                String ret = name + "(";
                for ( String s : parameters )
                {
                    ret += s + ",";
                }

                return ret.substring(0, ret.length() - 1) + ")";
            }
            else
            {
                return name;
            }
        }
    }
}
