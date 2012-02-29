/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 * ennum for operType in DataObjInp
 * from ./iRODS/lib/api/include/dataObjInpOut.h
#define DONE_OPR                9999
#define PUT_OPR                 1
#define GET_OPR                 2
#define SAME_HOST_COPY_OPR      3
#define COPY_TO_LOCAL_OPR       4
#define COPY_TO_REM_OPR         5
#define REPLICATE_OPR           6
#define REPLICATE_DEST          7
#define REPLICATE_SRC           8
#define COPY_DEST               9
#define COPY_SRC                10
#define RENAME_DATA_OBJ         11
#define RENAME_COLL             12
#define MOVE_OPR                13
#define RSYNC_OPR               14
#define PHYMV_OPR               15
#define PHYMV_SRC               16
#define PHYMV_DEST              17
#define QUERY_DATA_OBJ          18
#define QUERY_DATA_OBJ_RECUR    19
#define QUERY_COLL_OBJ          20
#define QUERY_COLL_OBJ_RECUR    21
 * @author toaster
 */
public enum OprTypeEnum
{
    NO_OPR_TYPE(0),
    DONE_OPR(9999),
    PUT_OPR(1),
    GET_OPR(2),
    SAME_HOST_COPY_OPR(3),
    COPY_TO_LOCAL_OPR(4),
    COPY_TO_REM_OPR(5),
    REPLICATE_OPR(6),
    REPLICATE_DEST(7),
    REPLICATE_SRC(8),
    COPY_DEST(9),
    COPY_SRC(10),
    RENAME_DATA_OBJ(11),
    RENAME_COLL(12),
    MOVE_OPR(13),
    RSYNC_OPR(14),
    PHYMV_OPR(15),
    PHYMV_SRC(16),
    PHYMV_DEST(17),
    QUERY_DATA_OBJ(18),
    QUERY_DATA_OBJ_RECUR(19),
    QUERY_COLL_OBJ(20),
    QUERY_COLL_OBJ_RECUR(21);
    int i;

    OprTypeEnum(int i)
    {
        this.i = i;
    }

    public OprTypeEnum valueOf(int i)
    {
        switch (i)
        {
            case 0:
                return NO_OPR_TYPE;
            case 9999:
                return DONE_OPR;
            case 1:
                return PUT_OPR;
            case 2:
                return GET_OPR;
            case 3:
                return SAME_HOST_COPY_OPR;
            case 4:
                return COPY_TO_LOCAL_OPR;
            case 5:
                return COPY_TO_REM_OPR;
            case 6:
                return REPLICATE_OPR;
            case 7:
                return REPLICATE_DEST;
            case 8:
                return REPLICATE_SRC;
            case 9:
                return COPY_DEST;
            case 10:
                return COPY_SRC;
            case 11:
                return RENAME_DATA_OBJ;
            case 12:
                return RENAME_COLL;
            case 13:
                return MOVE_OPR;
            case 14:
                return RSYNC_OPR;
            case 15:
                return PHYMV_OPR;
            case 16:
                return PHYMV_SRC;
            case 17:
                return PHYMV_DEST;
            case 18:
                return QUERY_DATA_OBJ;
            case 19:
                return QUERY_DATA_OBJ_RECUR;
            case 20:
                return QUERY_COLL_OBJ;
            case 21:
                return QUERY_COLL_OBJ_RECUR;
            default:
                throw new IllegalArgumentException("Unknown type: " + i);
        }
    }

    public int getInt()
    {
        return i;
    }
}
