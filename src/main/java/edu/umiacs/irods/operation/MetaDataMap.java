/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation;

import edu.umiacs.irods.api.pi.ApiNumberEnum;
import edu.umiacs.irods.api.pi.GenQueryEnum;
import edu.umiacs.irods.api.pi.ModAVUMetadataInp_PI;
import edu.umiacs.irods.api.pi.ErrorEnum;
import edu.umiacs.irods.api.pi.ObjTypeEnum;
import edu.umiacs.irods.api.pi.RodsObjStat_PI;
import edu.umiacs.irods.operation.MetaDataMap.Entry;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Metadata map, backed by irods. Any put/changes to values here will be reflected
 * in iRODS.
 *
 * sending msg: attach data to a collection
<ModAVUMetadataInp_PI>
<arg0>add</arg0>
<arg1>-c</arg1>
<arg2>/chron-umiacs/home/rods/test</arg2>
<arg3>nme</arg3>
<arg4>val</arg4>
<arg5></arg5>
<arg6></arg6>
<arg7></arg7>
<arg8></arg8>
<arg9></arg9>
</ModAVUMetadataInp_PI>

 * list data on a collection:
 * sending msg:
<GenQueryInp_PI>
<maxRows>10</maxRows>
<continueInx>0</continueInx>
<partialStartIndex>0</partialStartIndex>
<options>0</options>
<KeyValPair_PI>
<ssLen>0</ssLen>
</KeyValPair_PI>
<InxIvalPair_PI>
<iiLen>3</iiLen>
<inx>610</inx>
<inx>611</inx>
<inx>612</inx>
<ivalue>0</ivalue>
<ivalue>0</ivalue>
<ivalue>0</ivalue>
</InxIvalPair_PI>
<InxValPair_PI>
<isLen>1</isLen>
<inx>501</inx>
<svalue>='/chron-umiacs/home/rods/test'</svalue>
</InxValPair_PI>
</GenQueryInp_PI>
 *
 * receive GenQueryOut_PI
 *
 * Attach data to a data item
 * sending msg:
<ModAVUMetadataInp_PI>
<arg0>add</arg0>
<arg1>-d</arg1>
<arg2>/chron-umiacs/home/rods/grant</arg2>
<arg3>nme</arg3>
<arg4>val</arg4>
<arg5></arg5>
<arg6></arg6>
<arg7></arg7>
<arg8></arg8>
<arg9></arg9>
</ModAVUMetadataInp_PI>

 *
 * Listing on data
 * <GenQueryInp_PI>
<maxRows>10</maxRows>
<continueInx>0</continueInx>
<partialStartIndex>0</partialStartIndex>
<options>0</options>
<KeyValPair_PI>
<ssLen>0</ssLen>
</KeyValPair_PI>
<InxIvalPair_PI>
<iiLen>3</iiLen>
<inx>600</inx>
<inx>601</inx>
<inx>602</inx>
<ivalue>0</ivalue>
<ivalue>0</ivalue>
<ivalue>0</ivalue>
</InxIvalPair_PI>
<InxValPair_PI>
<isLen>2</isLen>
<inx>501</inx>
<inx>403</inx>
<svalue>='/chron-umiacs/home/rods'</svalue>
<svalue>='grant'</svalue>
</InxValPair_PI>
</GenQueryInp_PI>

 * One limitation, this class assumes unique attribute names. iRODS allows
 * multiple copies of the attribtues, values, units. For example, you can have a
 * collection with teh following attribtues:
 *
attribute: f
value: f
units:
----
attribute: f
value: g
units:
----
attribute: nme
value: val
units:
----
attribute: f
value: g
units: g

 * Notice the overlap. If you attempt to access a file which contains duplicate
 * attributes, an exception will be thrown
 *
 * Class is not thread-safe
 * 
 * @author toaster
 */
public class MetaDataMap {

    private Map<String, Entry> cache = new HashMap<String, Entry>();
    private ConnectOperation connection;
    boolean isFile;
    private String colName;
    private String dataName;

    public MetaDataMap(String path, ConnectOperation connection) throws IOException {
        IrodsOperations ops = new IrodsOperations(connection);
        this.connection = connection;

        RodsObjStat_PI stat = ops.stat(path);
        if (stat == null) {
            throw new FileNotFoundException(path);
        }

        if (stat.getObjType() == ObjTypeEnum.COLL_OBJ_T) {
            isFile = false;
            colName = path;
        } else if (stat.getObjType() == ObjTypeEnum.DATA_OBJ_T) {
            isFile = true;
            int lastSlash = path.lastIndexOf("/");
            colName = path.substring(0, lastSlash);
            dataName = path.substring(lastSlash + 1);
        } else {
            throw new FileNotFoundException(path);
        }

        refresh();

    }

    public void refresh() throws IOException {
        cache.clear();
        if (isFile) {
            QueryBuilder qb = new QueryBuilder(GenQueryEnum.COL_META_DATA_ATTR_NAME,
                    GenQueryEnum.COL_META_DATA_ATTR_VALUE, GenQueryEnum.COL_META_DATA_ATTR_UNITS);
            qb.eq(GenQueryEnum.COL_COLL_NAME, colName);
            qb.eq(GenQueryEnum.COL_DATA_NAME, dataName);
            QueryResult qr = qb.execute(connection.getConnection());

            while (qr.next()) {
                Entry e = new Entry();
                e.key = qr.getValue(GenQueryEnum.COL_META_DATA_ATTR_NAME);
                e.units = qr.getValue(GenQueryEnum.COL_META_DATA_ATTR_UNITS);
                e.value = qr.getValue(GenQueryEnum.COL_META_DATA_ATTR_VALUE);
                if (cache.containsKey(e.key)) {
                    throw new IOException("Duplicate keys on object");
                }
                cache.put(e.key, e);
            }

        } else {
            QueryBuilder qb = new QueryBuilder(GenQueryEnum.COL_META_COLL_ATTR_NAME,
                    GenQueryEnum.COL_META_COLL_ATTR_VALUE, GenQueryEnum.COL_META_COLL_ATTR_UNITS);
            qb.eq(GenQueryEnum.COL_COLL_NAME, colName);
            QueryResult qr = qb.execute(connection.getConnection());

            while (qr.next()) {
                Entry e = new Entry();
                e.key = qr.getValue(GenQueryEnum.COL_META_COLL_ATTR_NAME);
                e.units = qr.getValue(GenQueryEnum.COL_META_COLL_ATTR_UNITS);
                e.value = qr.getValue(GenQueryEnum.COL_META_COLL_ATTR_VALUE);
                if (cache.containsKey(e.key)) {
                    throw new IOException("Duplicate keys on object");
                }

                cache.put(e.key, e);
            }
        }

    }

    public int size() {
        return cache.size();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public boolean containsValue(Object value) {
        for (Entry e : cache.values()) {
            if (e.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public void put(String key, String value, String units) {
        if (cache.containsKey(key)) {
            Entry e = cache.get(key);
            e.setUnits(units);
            e.setValue(value);
        } else {
            ModAVUMetadataInp_PI body;
            if (!isFile) {
                body = new ModAVUMetadataInp_PI("add", "-C",
                        colName, key, value, units, null, null, null, null);
            } else {
                body = new ModAVUMetadataInp_PI("add", "-d",
                        colName + "/" + dataName, key, value, units, null, null, null, null);
            }
            IrodsApiRequest apiReq = new IrodsApiRequest(ApiNumberEnum.MOD_AVU_METADATA_AN, body, null);

            try {
                int status = apiReq.sendRequest(connection.getConnection());

                if (status < 0) {
                    throw new RuntimeException("Irods Error: " + ErrorEnum.valueOf(status));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String get(String key) {
        return cache.get(key).getValue();
    }

    public void remove(String key) {
        Entry value = cache.get(key);


        if (value != null) {
            ModAVUMetadataInp_PI body;
            if (!isFile) {
                body = new ModAVUMetadataInp_PI("rm", "-C",
                        colName, value.key, value.value, value.units, null, null, null, null);
            } else {
                body = new ModAVUMetadataInp_PI("rm", "-d",
                        colName + "/" + dataName, value.key, value.value, value.units, null, null, null, null);
            }
            IrodsApiRequest apiReq = new IrodsApiRequest(ApiNumberEnum.MOD_AVU_METADATA_AN, body, null);

            try {
                int status = apiReq.sendRequest(connection.getConnection());

                if (status < 0) {
                    throw new RuntimeException("Irods Error: " + ErrorEnum.valueOf(status));
                }
                cache.remove(key);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
//            return null;
        }
    }

//    public void putAll(Map m) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
    public void clear() {
        Set<String> items = new HashSet<String>(cache.keySet());
        for (String key : items) {
            remove(key);
        }

    }

    public Set<String> keySet() {
        return Collections.unmodifiableSet(cache.keySet());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        for (Entry e : cache.values()) {
            sb.append(e);
            sb.append(",");
        }
        sb.append("}");
        return sb.toString();
    }
//    public Collection values() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    public Set entrySet() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    public class Entry implements Map.Entry<String, String> {

        private String key;
        private String value;
        private String units;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setUnits(String units) {
            ModAVUMetadataInp_PI body;
            if (!isFile) {
                body = new ModAVUMetadataInp_PI("mod", "-C",
                        colName, key, value, units, null, null, null, null);
            } else {
                body = new ModAVUMetadataInp_PI("mod", "-d",
                        colName + "/" + dataName, key, value, units, null, null, null, null);
            }
            IrodsApiRequest apiReq = new IrodsApiRequest(ApiNumberEnum.MOD_AVU_METADATA_AN, body, null);

            try {
                int status = apiReq.sendRequest(connection.getConnection());

                if (status < 0) {
                    throw new RuntimeException("Irods Error: " + ErrorEnum.valueOf(status));
                }
                this.units = units;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public String getUnits() {
            return units;
        }

        public String setValue(String value) {
            String old = this.value;
            ModAVUMetadataInp_PI body;
            if (!isFile) {
                body = new ModAVUMetadataInp_PI("mod", "-C",
                        colName, key, value, units, null, null, null, null);
            } else {
                body = new ModAVUMetadataInp_PI("mod", "-d",
                        colName + "/" + dataName, key, value, units, null, null, null, null);
            }
            IrodsApiRequest apiReq = new IrodsApiRequest(ApiNumberEnum.MOD_AVU_METADATA_AN, body, null);

            try {
                int status = apiReq.sendRequest(connection.getConnection());

                if (status < 0) {
                    throw new RuntimeException("Irods Error: " + ErrorEnum.valueOf(status));
                }
                this.value = value;
                return old;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return key + "=" + value + units;
        }
    }
}
