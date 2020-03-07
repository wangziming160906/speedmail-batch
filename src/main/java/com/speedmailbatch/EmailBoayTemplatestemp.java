package com.speedmailbatch;


/**
 * Created by wangshuai on 2017/9/13.
 */
public class EmailBoayTemplatestemp {

    public static String getBoay(){
        String body = "Dear manager,<br>" + "Thanks for giving me an opportunity  to promote our company Bosun mould.<br>" + "<br>" + "We specialize in below kinds of injection molds for exporting worldwide:<br>" + "<br>" + "- Common custom plastic molds<br>" + "- High precision molds<br>" + "- High Cavitation Injection Molds<br>" + "- Large size molds<br>" + "- Two Shot Molds<br>" + "- Unscrewing Molds<br>" + "- Gas Assist Molds<br>" + "- Die Casting tools<br>" + "<br>" + "If you are interested of us, please feel free to contact me for a quotation, we are all here to provide you service at any time, thank you!<br>" + "<br>" + "Sincerely,<br>" + "Sara Liu<br>" + "Overseas Marketing Dept.<br>" + "-------------------------------------------------------------------------------------------<br>" + "BOSUN Mould Technology Co.,LTD.<br>" + "Cell: 0086 18576459878  Skype:phyllis-Bosun Mould <br>" + "Tel: 0086-0755-28947686   Fax:0086-0755-28947976<br>" + "Add: #3-B,1 Road, Buxinji industrial, Guanjingtou, Fenggang, Dongguan city, Guangdong, China.";
        return body;
    }

    public static String getSubject(){
        return "Injection molds/molding/plastic parts";
    }

    public static String getContentReply(){

        return "你的邮件已收到,会尽快处理,谢谢！";
    }







}
