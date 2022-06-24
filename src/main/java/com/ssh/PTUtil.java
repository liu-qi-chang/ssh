package com.ssh;


import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.ping.*;

/**
 * 验证码只能简单识别
 * 暂不可用
 */
public class PTUtil {
    /***
     *  ping操作
     * @param hostname
     * @param timeout in milliseconds
     * @return
     */
    public static JsonResult pingResult(String hostname, Integer timeout){
        JsonResult jsonResult = new JsonResult();
        try {
            InetAddress address = InetAddress.getByName(hostname);
            boolean flag = address.isReachable(timeout);
            if(flag){
                jsonResult.setMessage("ping结果:the address is reachable.");
            }else{
                jsonResult.setCode(Constants.ResultCode.EXCEPTION);
                jsonResult.setMessage("ping结果:the address is unreachable.");
            }
        } catch (UnknownHostException e) {
            jsonResult.setCode(Constants.ResultCode.EXCEPTION);
            jsonResult.setMessage("ping结果:UnknownHostException:"+e.getMessage());
        } catch (IOException e) {
            jsonResult.setCode(Constants.ResultCode.EXCEPTION);
            jsonResult.setMessage("ping结果:IOException:"+e.getMessage());
        }
        return jsonResult;
    }
    /***
     *  telnet 操作
     * @param hostname
     * @param timeout in milliseconds
     * @return
     */
    public static JsonResult telnetResult(String hostname, Integer port, Integer timeout){
        JsonResult jsonResult = new JsonResult();
        try {
            Socket server = new Socket();
            InetSocketAddress address = new InetSocketAddress(hostname,port);
            server.connect(address, timeout);
            server.close();
            jsonResult.setMessage("telnet结果:success!");
        }catch (UnknownHostException e) {
            jsonResult.setCode(Constants.ResultCode.EXCEPTION);
            jsonResult.setMessage("telnet结果:UnknownHostException:"+e.getMessage());
        } catch (IOException e) {
            jsonResult.setCode(Constants.ResultCode.EXCEPTION);
            jsonResult.setMessage("telnet结果:IOException:"+e.getMessage());
        }
        return jsonResult;
    }

    public static void main(String[] args)
    {
        List<Integer> ports = new ArrayList<>();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(100);
        for(int i = 1; i<=9999; i++)
        {
            int finalI = i;
            executor.submit(()->{
                JsonResult jsonResult = telnetResult("140.25.65.122", 3306, 3000);
                System.out.println(jsonResult.getMessage()+":"+ finalI);
                if(jsonResult.getMessage().contains("success")){
                    ports.add(finalI);
                }
            });
        }
    }
}
