package com.awkrid.todo.domain.user.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class S3Service(
    private val amazonS3: AmazonS3,
    @Value("\${cloud.aws.s3.bucketName}")
    private val bucketName: String
) {
    companion object {
        private val ALLOWED_EXTENSION_LIST = listOf("jpg", "jpeg", "png")
    }

    fun upload(file: MultipartFile): String {
        checkFileNotEmpty(file)
        validateImageExtension(file.originalFilename)
        return uploadImage(file)

    }

    fun checkFileNotEmpty(file: MultipartFile) {
        if (file.isEmpty) throw RuntimeException("File can not be empty")
    }

    fun validateImageExtension(filename: String?) {
        val dotIndex = filename?.lastIndexOf('.') ?: throw RuntimeException("Image file not valid")
        if (dotIndex == -1) throw RuntimeException("Image file not valid")
        val extension = filename.substring(dotIndex + 1).lowercase()
        if (!ALLOWED_EXTENSION_LIST.contains(extension)) throw RuntimeException("Image file not valid")
    }

    fun uploadImage(file: MultipartFile): String {
        val fileName = file.originalFilename
        val objectMetadata = ObjectMetadata().apply {
            this.contentType = file.contentType
            this.contentLength = file.size
        }
        amazonS3.putObject(bucketName, fileName, file.inputStream, objectMetadata)
        return amazonS3.getUrl(bucketName, fileName).toString()
    }
}