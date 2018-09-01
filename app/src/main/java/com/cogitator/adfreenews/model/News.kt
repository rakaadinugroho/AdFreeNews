package com.cogitator.adfreenews.model

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 1/9/18 (MM/DD/YYYY)
 */

@Entity(tableName = "news")
data class News constructor(@PrimaryKey @ColumnInfo(name = "id") var id: String="", @SerializedName("source") @Embedded(prefix = "source_") val source: NewsSource?=null,
                            val author:String?=null,val title:String? =null,val description:String?=null,
                            val url : String?=null,val urlToImage: String?=null,val publishedAt:String="",var category:String?,var isBookmarked:Boolean=false){
}