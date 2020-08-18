package cc.foxa.kie

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CommentApi {
    @GET("/comment")
    suspend fun getComments(): List<Comment>

    @POST("/comment")
    suspend fun postComment(@Body comment: CommentToPost)

    @POST("/comment/enable")
    suspend fun enable(@Body deviceToken: DeviceInfo)

    @POST("/comment/disable")
    suspend fun disable(@Body deviceToken: DeviceInfo)

}