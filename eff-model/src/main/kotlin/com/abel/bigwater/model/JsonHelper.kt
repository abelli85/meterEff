package com.abel.bigwater.model

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object JsonHelper {

    const val FULL_DATE_FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    const val FULL_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX"

    const val FULL_DATE_FORMAT_AREA = "yyyy-MM-dd'T'HH:mm:ss.SSS"

    const val DATE_SECOND_FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    const val DATE_SECOND_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX"

    const val DATE_SECOND_FORMAT_AREA = "yyyy-MM-dd'T'HH:mm:ss"

    const val DATE_MIN_FORMAT_ISO = "yyyy-MM-dd'T'HH:mm'Z'"

    const val DATE_MIN_FORMAT = "yyyy-MM-dd'T'HH:mmX"

    const val DATE_MIN_FORMAT_AREA = "yyyy-MM-dd'T'HH:mm"

    const val DATE_FORMAT_ISO = "yyyy-MM-ddZ"

    const val DATE_FORMAT = "yyyy-MM-ddX"

    const val DATE_FORMAT_AREA = "yyyy-MM-dd"


    const val LOCAL_FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSX"

    const val LOCAL_FULL_DATE_FORMAT_AREA = "yyyy-MM-dd HH:mm:ss.SSS"

    const val LOCAL_DATE_SECOND_FORMAT = "yyyy-MM-dd HH:mm:ssX"

    const val LOCAL_DATE_SECOND_FORMAT_AREA = "yyyy-MM-dd HH:mm:ss"

    const val LOCAL_DATE_MIN_FORMAT = "yyyy-MM-dd HH:mmX"

    const val LOCAL_DATE_MIN_FORMAT_AREA = "yyyy-MM-dd HH:mm"

    /**
     * parses string with iso-date-format/other precise date-format/default date-format.
     * @param str
     * @param dv
     * @return
     */
    fun parseISODate(str: String?): Date? {
        str ?: return null

        try {
            return SimpleDateFormat(FULL_DATE_FORMAT_ISO).apply {
                timeZone = TimeZone.getTimeZone("+08")
            }.parse(str)
        } catch (ex: Exception) {
            // nothing.
        }

        try {
            return SimpleDateFormat(FULL_DATE_FORMAT).parse(str)
        } catch (ex: Exception) {
            // nothing.
        }

        try {
            return SimpleDateFormat(FULL_DATE_FORMAT_AREA).parse(str)
        } catch (ex: Exception) {
            // nothing.
        }

        // if invalid 1.
        try {
            return SimpleDateFormat(DATE_SECOND_FORMAT_ISO).apply {
                timeZone = TimeZone.getTimeZone("+08")
            }.parse(str)
        } catch (ex: Exception) {
            // nothing.
        }

        // if invalid 2.
        try {
            return SimpleDateFormat(DATE_SECOND_FORMAT).parse(str)
        } catch (ex: Exception) {
            // nothing.
        }

        // if no area & no millis.
        try {
            return SimpleDateFormat(DATE_SECOND_FORMAT_AREA).parse(str)
        } catch (ex: Exception) {
            // nothing.
        }

        try {
            return SimpleDateFormat(DATE_MIN_FORMAT_ISO).apply {
                timeZone = TimeZone.getTimeZone("+08")
            }.parse(str)
        } catch (ex: Exception) {
            // nothing.
        }

        // if no seconds
        try {
            return SimpleDateFormat(DATE_MIN_FORMAT).parse(str)
        } catch (ex: Exception) {
            // nothing.
        }

        // if no seconds & area
        try {
            return SimpleDateFormat(DATE_MIN_FORMAT_AREA).parse(str)
        } catch (ex: Exception) {
            // nothing.
        }

        try {
            return SimpleDateFormat(DATE_FORMAT_ISO).apply {
                timeZone = TimeZone.getTimeZone("+08")
            }.parse(str)
        } catch (e1: Exception) {
        }

        try {
            return SimpleDateFormat(DATE_FORMAT).parse(str)
        } catch (e1: Exception) {
        }

        try {
            return SimpleDateFormat(DATE_FORMAT_AREA).parse(str)
        } catch (e1: Exception) {
        }

        return parseLocalDate(str)
    }

    /**
     * 常用日期格式解析：yyyy-MM-dd HH:mm:ss.SSSX.
     */
    fun parseLocalDate(str: String?): Date? {
        str ?: return null

        try {
            return SimpleDateFormat(LOCAL_FULL_DATE_FORMAT).parse(str)
        } catch (e1: Exception) {
        }

        try {
            return SimpleDateFormat(LOCAL_FULL_DATE_FORMAT_AREA).parse(str)
        } catch (e1: Exception) {
        }

        try {
            return SimpleDateFormat(LOCAL_DATE_SECOND_FORMAT).parse(str)
        } catch (e1: Exception) {
        }

        try {
            return SimpleDateFormat(LOCAL_DATE_SECOND_FORMAT_AREA).parse(str)
        } catch (e1: Exception) {
        }

        try {
            return SimpleDateFormat(LOCAL_DATE_MIN_FORMAT).parse(str)
        } catch (e1: Exception) {
        }

        try {
            return SimpleDateFormat(LOCAL_DATE_MIN_FORMAT_AREA).parse(str)
        } catch (e1: Exception) {
        }

        try {
            return SimpleDateFormat(DATE_FORMAT).parse(str)
        } catch (e1: Exception) {
        }

        try {
            return SimpleDateFormat(DATE_FORMAT_AREA).parse(str)
        } catch (e1: Exception) {
        }

        try {
            return DateFormat.getInstance().parse(str)
        } catch (ex: Exception) {
            // does nothing
        }

        return null
    }

    /**
     * 获取IP的城市
     */
    fun getCity(ip: String?): String? {
        if (ip.isNullOrBlank()) return "(空)"

        val url = "http://ip.taobao.com/service/getIpInfo.php?ip="
        val json = getResult(url + ip.orEmpty(), "", Charsets.UTF_8.name())
        val to = JSON.parseObject(json)
        val jo = to?.get("data") as JSONObject?
        val city = "${jo?.get("country")}#${jo?.get("city")}#${jo?.get("isp")}"
        return city
    }

    /**
     * @param urlStr
     *   请求的地址
     * @param content
     *   请求的参数 格式为：name=xxx&pwd=xxx
     * @param encoding
     *   服务器端请求编码。如GBK,UTF-8等
     * @return
     */
    fun getResult(urlStr: String, content: String, encoding: String): String? {
        var url: URL? = null
        var connection: HttpURLConnection? = null;
        try {
            url = URL(urlStr)
            connection = url.openConnection() as HttpURLConnection // 新建连接实例
            connection.connectTimeout = 2000                     // 设置连接超时时间，单位毫秒
            connection.readTimeout = 2000                        // 设置读取数据超时时间，单位毫秒
            connection.doOutput = true                           // 是否打开输出流 true|false
            connection.doInput = true                            // 是否打开输入流true|false
            connection.requestMethod = "POST"                    // 提交方法POST|GET
            connection.useCaches = false                         // 是否缓存true|false
            connection.connect()                                   // 打开连接端口
            val out = DataOutputStream(connection.outputStream)    // 打开输出流往对端服务器写数据
            out.writeBytes(content)                                // 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush()                                            // 刷新
            out.close()                                            // 关闭输出流
            val reader = BufferedReader(InputStreamReader(connection.inputStream, encoding))// 往对端写完数据对端服务器返回数据 ,以BufferedReader流来读取
            val buffer = StringBuffer()
            var line = reader.readLine()
            while (line != null) {
                buffer.append(line)
                line = reader.readLine()
            }
            reader.close()
            return buffer.toString()
        } catch (e: IOException) {
            throw e
        } finally {
            connection?.disconnect()
        }
    }
}