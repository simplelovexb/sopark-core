package cn.suxiangbao.sopark.mongo;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mongodb.*;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sxb on 2017/2/13.
 */
public class MongoUtil {
    private static final Logger logger = LoggerFactory.getLogger(MongoUtil.class);

    // MongoDB--Operators
    public static final String OP_INC = "$inc";
    public static final String OP_PUSH = "$push";
    public static final String OP_PULL = "$pull";
    public static final String OP_ADD_TO_SET = "$addToSet";
    public static final String OP_EACH = "$each";
    public static final String OP_EXISTS = "$exists";
    public static final String OP_SLICE = "$slice";
    public static final String OP_SET = "$set";
    public static final String OP_UNSET = "$unset";
    public static final String OP_MATCH = "$match";
    public static final String OP_GROUP = "$group";
    public static final String OP_SORT = "$sort";
    public static final String OP_IN = "$in";
    public static final String OP_SUM = "$sum";
    public static final String OP_NE = "$ne";
    public static final String OP_LT = "$lt";
    public static final String OP_LTE = "$lte";
    public static final String OP_GTE = "$gte";
    public static final String OP_GT = "$gt";
    public static final String OP_FIRST = "$first";
    public static final String OP_LAST = "$last";
    public static final String OP_MAX = "$max";
    public static final String OP_MIN = "$min";
    public static final String OP_LIMIT = "$limit";
    public static final String OP_REGEX = "$regex";
    public static final String OP_SET_ON_INSERT = "$setOnInsert";

    public static final String GEO_TYPE = "type";// Geo类型
    public static final String GEO_TYPE_POINT = "Point";// Geo点类型
    public static final String GEO_COORDINATES = "coordinates";// 经纬度（先是经度，后是纬度）
    public static final String GEO_CENTER_SPHERE = "$centerSphere";


    public static final String CMD_GEO_NEAR = "near";
    public static final String CMD_GEO_GEONEAR = "geoNear";
    public static final String CMD_GEO_SPHERICAL = "spherical";
    public static final String CMD_GEO_QUERY = "query";
    public static final String CMD_GEO_MAXDISTANCE = "maxDistance";
    public static final String CMD_GEO_MINDISTANCE = "minDistance";
    public static final String CMD_GEO_LIMIT = "limit";
    public static final String CMD_GEO_WITHIN = "$geoWithin";


    public static final String CMD_RET_GEO_RESULTS = "results";
    public static final String CMD_RET_GEO_RESULTS_DIS = "dis";
    public static final String CMD_RET_GEO_RESULTS_OBJ = "obj";

    // MongoDB--General Fields
    public static final String FIELD_OBJ_ID = "_id";
    public static final String FIELD_CREATE_TIME = "createTime";
    public static final String FIELD_UPDATE_TIME = "updateTime";
    public static final String FIELD_SERVER = "_ttserver";


    /**
     * 强制将idName的属性转换成mongodb中的_id，而且类型为ObjectId
     *
     * @param mongoTemplate
     * @param javaObj
     * @param idName
     * @return
     * @throws Exception
     */
    public static <T> DBObject javaObj2Db(MongoTemplate mongoTemplate, T javaObj, String idName) {
        DBObject obj = (DBObject) mongoTemplate.getConverter().convertToMongoType(javaObj);
        if (idName != null) {
            Object objectId = obj.removeField(idName);
            if (objectId instanceof String) {
                obj.put(FIELD_OBJ_ID, new ObjectId(objectId.toString()));
            } else if (objectId instanceof Long) {
                obj.put(FIELD_OBJ_ID, objectId);
            }
        }
        return obj;
    }


    /**
     * 强制转换Mongodb里面的数据为业务应用的Java对象，并且转换前先将_id属性转换为idName属性
     * 注意不要改变obj里面的内容，会影响到其他代码（Guava的代码是使用时才动态计算的列表）
     *
     * @param mongoTemplate
     * @param toClass
     * @param obj
     * @param idName
     * @return
     * @throws Exception
     */
    public static <T> T db2JavaObj(MongoTemplate mongoTemplate, Class<T> toClass, DBObject obj, String idName) throws Exception {
        if (obj == null) {
            return null;
        }
        Object tmpObj = obj.removeField(FIELD_OBJ_ID);
        if (idName != null) {
            if (tmpObj instanceof ObjectId) {
                obj.put(idName, ((ObjectId) tmpObj).toHexString());
            } else if (tmpObj != null) {
                obj.put(idName, tmpObj);
            }
        }

        T t = mongoTemplate.getConverter().read(toClass, obj);
        if (idName != null) { // 这里进行还原
            obj.removeField(idName);
        }
        obj.put(FIELD_OBJ_ID, tmpObj);
        return t;
    }

    public static Long getLong(Object obj, Long defaultValue) {
        Long ret = defaultValue;
        if (obj instanceof Integer) {
            ret = ((Integer) obj).longValue();
        } else if (obj instanceof Long) {
            ret = (Long) obj;
        } else if (obj instanceof Double) {
            ret = ((Double) obj).longValue();
        }
        return ret;
    }

    public static Long getNotNegativeLong(Object obj) {
        Long ret = getLong(obj,0L);
        if (ret <0 ){
            ret = 0L;
        }
        return  ret;
    }

    public static Integer getInt(Object obj, Integer defaultValue) {
        Integer ret = defaultValue;
        if (obj instanceof Integer) {
            ret = (Integer) obj;
        } else if (obj instanceof Long) {
            ret = ((Long) obj).intValue();
        } else if (obj instanceof Double) {
            ret = ((Double) obj).intValue();
        }
        return ret;
    }

    public static Boolean getBoolean(Object obj, Boolean defaultValue) {
        Boolean ret = defaultValue;
        if (obj instanceof Boolean) {
            ret = (Boolean) obj;
        }
        return ret;
    }

    public static Double getDouble(Object obj, Double defaultValue) {
        Double ret = defaultValue;
        if (obj instanceof Integer) {
            ret = ((Integer) obj).doubleValue();
        } else if (obj instanceof Long) {
            ret = ((Long) obj).doubleValue();
        } else if (obj instanceof Double) {
            ret = (Double) obj;
        }
        return ret;
    }

    private static final AtomicInteger OBJ_NEXT_COUNTER = new AtomicInteger(new SecureRandom().nextInt());
    private static int OBJ_MACHINE_IDENTIFIER = ObjectId.getGeneratedMachineIdentifier();

    public static void resetMachineId(Integer mid) {
        if (mid == null) {// 人工设置的mid不合格！
            logger.warn("Machine Id Not Set! mid is null! default mid:{}", OBJ_MACHINE_IDENTIFIER);
            return;
        }
        if (mid % 100 == 0) {
            logger.warn("The Machine is local! mid=" + mid);
        } else if (mid % 100 == 1) {
            logger.warn("The MachineId is not set! mid=" + mid);
        }
        OBJ_MACHINE_IDENTIFIER = mid;
        logger.info("Machine Id:{}, ProcessId:{}", OBJ_MACHINE_IDENTIFIER, ObjectId.getGeneratedProcessIdentifier());

        if (mid <= 0 || mid >= 1000) {// 人工设置的mid不合格！
            logger.error("Machine Id Not Set Right! mid:{}, default mid:{}", mid, OBJ_MACHINE_IDENTIFIER);
        }
    }

    static {
        logger.info("Machine Id:{}, ProcessId:{}", OBJ_MACHINE_IDENTIFIER, ObjectId.getGeneratedProcessIdentifier());
    }

    public static ObjectId genObjId() {
        return new ObjectId(new Date(), OBJ_MACHINE_IDENTIFIER, (short) ObjectId.getGeneratedProcessIdentifier(),
                OBJ_NEXT_COUNTER.getAndIncrement() & 0x00ffffff);
    }

    private static ObjectId genObjId(Integer mid) {
        if (mid == null) {
            mid = OBJ_MACHINE_IDENTIFIER;
        }
        return new ObjectId(new Date(), mid, (short) ObjectId.getGeneratedProcessIdentifier(), OBJ_NEXT_COUNTER.getAndIncrement() & 0x00ffffff);
    }

    public static ObjectId genObjIdB4Millis(long b4Millis) {
        return new ObjectId(new Date(System.currentTimeMillis() - b4Millis), 0, (short) 0, 0);
    }

    public static ObjectId genObjIdInDate(Date date) {
        return new ObjectId(date, 0, (short) 0, 0);
    }

    private static int genOwnerTtServer(long ownerId) {
        if (ownerId >= 200000000L) {
            return 200;
        }
        return 100;
    }

    public static DBObject appendOwnerTtServerId(long ownerId, DBObject obj) {
        if (obj == null) {
            return null;
        }

        obj.put(FIELD_SERVER, genOwnerTtServer(ownerId));
        return obj;
    }

    public static void removeServerData(MongoTemplate mongoTemplate, String collectionName, DBObject query) {
        mongoTemplate.getDb().getCollection(collectionName).remove(query, WriteConcern.UNACKNOWLEDGED);
    }

    public static QueryBuilder genQb2RemoveOtherDist(long owner, String objIdStr) {
        return genQb2RemoveOtherDist(owner, FIELD_OBJ_ID, objIdStr);
    }

    public static QueryBuilder genQb2RemoveOtherDist(long owner, String fieldName, String objIdStr) {
        if (objIdStr == null) {
            return null;
        }
        ObjectId objId = new ObjectId(objIdStr);
        QueryBuilder query = QueryBuilder.start(fieldName).is(objId);
        query.and(FIELD_SERVER).notEquals(genOwnerTtServer(owner));
        return query;
    }

    public static BasicDBObject genWithObjId(String similarObjId) {
        BasicDBObject obj = new BasicDBObject();
        appendObjId(obj, similarObjId);
        return obj;
    }

    public static BasicDBObject genWithObjId(long ownerId) {
        BasicDBObject obj = new BasicDBObject();
        appendObjId(obj, ownerId);
        return obj;
    }

    public static BasicDBObject genWithObjId() {
        BasicDBObject obj = new BasicDBObject();
        appendObjId(obj);
        return obj;
    }


    public static ObjectId appendObjId(DBObject obj) {
        if (obj == null) {
            return null;
        }
        ObjectId objId = genObjId();
        obj.put(FIELD_OBJ_ID, objId);
        obj.put(FIELD_SERVER, objId.getMachineIdentifier());
        return objId;
    }

    /**
     * 有可能产生全局不一致性，但当然，前提是进程号相同！
     *
     * @param obj
     * @param similarObjId
     * @return
     */
    public static ObjectId appendObjId(DBObject obj, String similarObjId) {
        if (obj == null) {
            return null;
        }
        ObjectId objId = genObjId(new ObjectId(similarObjId).getMachineIdentifier());
        obj.put(FIELD_OBJ_ID, objId);
        obj.put(FIELD_SERVER, objId.getMachineIdentifier());
        return objId;
    }


    public static ObjectId appendObjId(DBObject obj, long ownerId) {
        if (obj == null) {
            return null;
        }
        ObjectId objId = genObjId(genOwnerTtServer(ownerId));
        obj.put(FIELD_OBJ_ID, objId);
        obj.put(FIELD_SERVER, objId.getMachineIdentifier());
        return objId;
    }


    public static QueryBuilder genQueryBuilderById(String objIdStr) {
        return genQueryBuilderById(FIELD_OBJ_ID, objIdStr);
    }

    public static QueryBuilder genQueryBuilderById(String fieldName, String objIdStr) {
        if (objIdStr == null) {
            return null;
        }
        ObjectId objId = new ObjectId(objIdStr);
        QueryBuilder query = QueryBuilder.start(fieldName).is(objId);
        query.and(FIELD_SERVER).is(objId.getMachineIdentifier());
        return query;
    }


    public static QueryBuilder genQueryBuilderById(String fieldName, long uid, int ttserver) {
        QueryBuilder query = QueryBuilder.start(fieldName).is(uid);
        query.and(FIELD_SERVER).is(ttserver);
        return query;
    }

    public static QueryBuilder genQueryBuilderById(String fieldName, long uid, String similarObjId) {
        QueryBuilder query = QueryBuilder.start(fieldName).is(uid);
        query.and(FIELD_SERVER).is(new ObjectId(similarObjId).getMachineIdentifier());
        return query;
    }

    public static QueryBuilder genQueryBuilderByIds(List<String> objIdStrs) {
        if (objIdStrs == null || objIdStrs.isEmpty()) {
            return null;
        }
        QueryBuilder query = QueryBuilder.start();
        List<ObjectId> objIds = Lists.transform(objIdStrs, new Function<String, ObjectId>() {
            @Override
            public ObjectId apply(String input) {
                return new ObjectId(input);
            }
        });
        BasicDBList ids = new BasicDBList();
        ids.addAll(objIds);
        query.and(FIELD_OBJ_ID);
        query.in(ids);
        return query;
    }


    public static QueryBuilder genNotInQueryBuilderByIds(List<String> objIdStrs) {
        if (objIdStrs == null || objIdStrs.isEmpty()) {
            return null;
        }
        QueryBuilder query = QueryBuilder.start();
        List<ObjectId> objIds = Lists.transform(objIdStrs, new Function<String, ObjectId>() {
            @Override
            public ObjectId apply(String input) {
                return new ObjectId(input);
            }
        });
        BasicDBList ids = new BasicDBList();
        ids.addAll(objIds);
        query.and(FIELD_OBJ_ID);
        query.notIn(ids);
        return query;
    }

    public static Object genLogObj(Object[] data) {
        return new InnerObj(data);
    }

    public static Object genLogObj(Collection<?> data) {
        return new InnerObj(data);
    }

    public static <K, V> Object genLogObj(Map<K, V> data) {
        return new InnerObj(data);
    }


    private static class InnerObj {
        private Object data;

        private InnerObj(Object data) {
            super();
            this.data = data;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public String toString() {
            if (data instanceof Map) {
                return genMapStr((Map) data);
            }
            if (data instanceof Collection) {
                return genColStr((Collection) data);
            }
            return "";
        }
    }


    private static String genColStr(Collection<?> ret) {
        if (ret == null || ret.isEmpty()) {
            return "col size: 0";
        }
        StringBuffer sb = new StringBuffer(" Collection size: ").append(ret.size()).append(", content: ");
        int min = Math.min(ret.size(), 5);
        Iterator<?> it = ret.iterator();
        sb.append("[").append(it.next());
        for (int i = 1; i < min; i++) {
            sb.append(", ").append(it.next());
        }
        sb.append("]");
        return sb.toString();
    }

    private static <K, V> String genMapStr(Map<K, V> ret) {
        if (ret == null || ret.isEmpty()) {
            return "map size: 0";
        }
        StringBuffer sb = new StringBuffer("map size: ").append(ret.size()).append(", content: ");
        int min = Math.min(ret.size(), 5);
        Iterator<Map.Entry<K, V>> it = ret.entrySet().iterator();
        sb.append("[").append(it.next());
        for (int i = 1; i < min; i++) {
            Map.Entry<K, V> entry = it.next();
            sb.append(", ").append(entry.getKey()).append("=").append(entry.getValue());
        }
        sb.append("]");
        return sb.toString();
    }

}
