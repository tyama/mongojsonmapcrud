package net.xmldo.mongo

import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBCursor
import com.mongodb.WriteResult
import org.bson.types.ObjectId
import org.codehaus.jackson.map.ObjectMapper

/**
 * http://api.mongodb.org/java/current/index.html
 * @author Tsuyoshi Yamamoto
 * @since 2010/12/01 11:12:52
 */
class FullMapController {
	final static ObjectMapper mapper = new ObjectMapper()
	final static String DBNAME = 'grailsfoo'
	final static String DBCOLLECTION = 'fullmapdata'
	//Low level API
	def mongo

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	/**
	 * common method for retrieve DBCollection
	 * @return DBCollection
	 */
	private DBCollection getDBCollection(){
		//DB
		def db = mongo.getDB(DBNAME)
		//Collection
		return db[DBCOLLECTION]
	}

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		params.offset = params.offset ? params.int('offset') : 0
		def collection = this.getDBCollection()

		DBCursor dbCursor = collection.find().skip(params.offset).limit(params.max)
		def list = dbCursor.toArray()

        [list: list, count: dbCursor.count()]
    }

    def create = {
		params.content='{"name":"put your json data"}'
    }

    def save = {
		if(params.content && params.content.startsWith('{')){
			def collection = this.getDBCollection()
			def map = mapper.readValue(params.content,Map.class)
			def dbobj =  new BasicDBObject(map)
			WriteResult result = collection << dbobj
			if(!result.getError()){
				flash.message = "${message(code: 'default.created.message', args: [message(code: 'fullMap.label', default: 'FullMap'), dbobj._id])}"
				redirect(action: "show", id: dbobj._id)
			}else{
				flash.message = "save error ${result.getError()}"
				render(view: "create", params:[content:params.content])
			}
		}else{
			flash.message = "save error content must starts with { or valid json data"
			render(view: "create", params:[content:params.content])
		}
    }

    def show = {
		def map = this.getDBCollection().findOne([_id:new ObjectId(params.id)])
		if(map){
			[map: map]
		}else{
			redirect(action: "list")
		}
    }

    def edit = {
        def map = this.getDBCollection().findOne([_id:new ObjectId(params.id)])
        if (!map) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'fullMap.label', default: 'FullMap'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [map: map]
        }
    }

    def update = {
		def collection = this.getDBCollection()
        def map = collection.findOne([_id:new ObjectId(params.id)])
        if (map) {
            if(params.content && params.content.startsWith('{')) {
				def data = mapper.readValue(params.content,Map.class)
				WriteResult result = collection.update(["_id":map._id],data)
				if(!result.getError()){
					flash.message = "${message(code: 'default.updated.message', args: [message(code: 'fullMap.label', default: 'FullMap'), map._id])}"
	                redirect(action: "show", id:params.id)
				}else{
					flash.message = "save error ${result.getError()}"
					render(view: "create", params:[content:params.content])
				}
				render(text:"value")
            }
            else {
				flash.message = "save error content must starts with { or valid json data"
                render(view: "edit", params:params)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'fullMap.label', default: 'FullMap'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
		def collection = this.getDBCollection()
        def map = collection.findOne([_id:new ObjectId(params.id)])
        if (map) {
			WriteResult result = collection.remove(map)
			if(!result.getError()){
				flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'fullMap.label', default: 'FullMap'), params.id])}"
				redirect(action: "list")
			}else{
				redirect(action: "show", id: params.id)
			}
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'fullMap.label', default: 'FullMap'), params.id])}"
            redirect(action: "list")
        }
    }

	private static String escapeRegex(String s) {
		StringBuilder sb = new StringBuilder()
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
				|| c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
				|| c == '*' || c == '?' || c == '|' || c == '&') { sb.append('\\') }
			sb.append(c)
		}
		return sb.toString()
	}
}
