/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 * lib/core/include/rodsType.h
 *typedef enum {
 *   UNKNOWN_OBJ_T,
 *   DATA_OBJ_T,
 *   COLL_OBJ_T,
 *   UNKNOWN_FILE_T,
 *   LOCAL_FILE_T,
 *   LOCAL_DIR_T,
 *   NO_INPUT_T,
 *} objType_t;
 * @author toaster
 */
public enum ObjTypeEnum
{

    UNKNOWN_OBJ_T(0),
    DATA_OBJ_T(1),
    COLL_OBJ_T(2),
    UNKNOWN_FILE_T(3),
    LOCAL_FILE_T(4),
    LOCAL_DIR_T(5),
    NO_INPUT_T(6);
    private int i;

    ObjTypeEnum(int i)
    {
        this.i = i;
    }

    public static ObjTypeEnum valueOf(int i)
    {
        switch (i)
        {
            case 0:
                return UNKNOWN_OBJ_T;
            case 1:
                return DATA_OBJ_T;
            case 2:
                return COLL_OBJ_T;
            case 3:
                return UNKNOWN_FILE_T;
            case 4:
                return LOCAL_FILE_T;
            case 5:
                return LOCAL_DIR_T;
            case 6:
                return NO_INPUT_T;
            default:
                throw new IllegalArgumentException("No type for :" + i);
        }
    }

    public int getInt()
    {
        return i;
    }
}
