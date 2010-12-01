package net.xmldo.mongo

class FullMap {
	String content
    static constraints = {
		content nullable:false,blank:false,maxSize:80000
    }
}
