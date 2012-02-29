/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

import java.util.ArrayList;
import java.util.List;

/**
 * #define TagStruct_PI "int ssLen; str *preTag[ssLen]; str *postTag[ssLen]; str *keyWord[ssLen];" 
 * @author toaster
 */
public class TagStruct implements IRodsPI {

    private List<String> preTag;
    private List<String> postTag;
    private List<String> keyWord;

    public TagStruct(List<String> preTag, List<String> postTag, List<String> keyWord)
    {
        this.preTag = preTag;
        this.postTag = postTag;
        this.keyWord = keyWord;
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<String> getKeyWord()
    {
        return new ArrayList<String>(keyWord);
    }

    public List<String> getPostTag()
    {
        return new ArrayList<String>(postTag);
    }

    public List<String> getPreTag()
    {
        return new ArrayList<String>(preTag);
    }
    
    public int getSsLen()
    {
        return keyWord.size();
    }
}
