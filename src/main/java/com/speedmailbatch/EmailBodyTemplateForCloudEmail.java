package com.speedmailbatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by wangshuai on 2018/1/28.
 */
public class EmailBodyTemplateForCloudEmail {

    private static final String BODY_HEAD = "<html><div style='font-size:13px;'><p>";

    private static final String BODY_END = "</p></div></html>";

    private static final String[] LIST_TEMPLATES_NEW ={
            "Dear manager,<br>" + "Thanks for giving me an opportunity  to promote our company Bosun mould.<br>" + "<br>" + "We specialize in below kinds of injection molds for exporting worldwide:<br>" + "<br>" + "- Common custom plastic molds<br>" + "- High precision molds<br>" + "- High Cavitation Injection Molds<br>" + "- Large size molds<br>" + "- Two Shot Molds<br>" + "- Unscrewing Molds<br>" + "- Gas Assist Molds<br>" + "- Die Casting tools<br>" + "<br>" + "If you are interested of us, please feel free to contact me for a quotation, we are all here to provide you " + "service at any time, thank you!",
            "Greetings, <br>" + "Please allow me to promote our company to you.<br>" + "BOSUN Molud is your one-stop shop for your molds & molding products needs. <br>" + "we works:<br>" + "<br>" + " 1) Cold & Hot Runner / Single & Multi-Cavity<br>" + " 2) Single & Multi-Shot<br>" + " 3) Die Cast-Aluminum & Zinc<br>" + " 4) Insert Molds&blow molding<br>" + " 5) Prototype Tooling<br>" + " 6) Over molding &stamping<br>" + " 7) Mass production for plastic parts or metal parts<br>" + "If you are interested of us, please feel free to contact me for a quotation, we are all here to provide you service at any time, thank you!",
            "Greetings,<br>" + "I am writing to you to thank you for giving me an opportunity to promote our company Bosun mould.<br>" + "Bosun mould is a very professional mould manufacturer in China.<br>" + "<br>" + "We offer as followings:<br>" + "<br>" + "1.Injection Molds;<br>" + "2.Die casting molds;<br>" + "3.Multi-material/Multi-color Molds;<br>" + "4.Prototype Tooling;<br>" + "5.Compression Molds;<br>" + "6.Stack Molds and etc.<br>" + "If you are interested of us, please don't hesitate to contact me for a quotation.",
            "Dear manager,<br>" + "Thanks for giving me an opportunity  to promote our company Bosun mould.<br>" + "<br>" + "BOSUN mould is a very professional  molds or molded products manufacturer in china, We offer as followings:<br>" + "1.Plastic injection Mold(plastic parts);<br>" + "2.Die Casting Mold and Die Casting Parts(Aluminum and Zinc);<br>" + "3.Inserts&blow mold;<br>" + "4.Prototype Tooling;<br>" + "5.Over molding &stamping<br>" + "6.Mass production for plastic parts or metal parts<br>" + "We also have our own injection room that can provide you one-stop service .<br>" + "<br>" + "If you are interested of us, please feel free to contact me for a quotation, we are all here to provide you service at any time, thank you!",
            "Greetings !<br>" + "Thanks for allowing me to promote our company Bosun mould.<br>" + "If you have a project that need mould , i recommend you to choose BOSUN MOULD, We offer as followings:<br>" + "1) Cold & Hot Runner / Single & Multi-Cavity<br>" + "2) Single & Multi-Shot<br>" + "3) Die Cast-Aluminum & Zinc<br>" + "4) Inserts<br>" + "5) Prototype Tooling<br>" + "6) Over molding<br>" + "7) Mass production for plastic parts or metal parts<br>" + "We also have own injection room that can provide you one-stop service .<br>" + "<br>" + "If you are interested of us, please feel free to contact me for a quotation, thank you!",
            "Greetings ,<br>" + "Please allow me to promote our company BOSUN MOULD.<br>" + "BOSUN Mould specialize in mould making ,Covering automotive, electronics, toys, Medical Items, family appliance, cosmetics and etc.<br>" + "we works:<br>" + "1.Injection Molds;<br>" + "2.Multi-material/Multi-color Molds;<br>" + "3.Prototype Tooling;<br>" + "5.Compression Molds;<br>" + "6.Stack Molds and etc.<br>" + "We also have our own injection room that can provide you one-stop service .<br>" + "<br>" + "If you are interested of us, please feel free to contact me for a quotation, thank you!",
            "Dear Sir,<br>" + "Thanks for giving  me an opportunity to promote our company Bosun mould.<br>" + "BOSUN MOULD is a professional plastic and metal molds factory, also we are the injection molding and metal die casting manufacturer.<br>" + "<br>" + "If you need to produce the plastic and metal products, contain the development and design service, Please send your 3D/2D drawings or samples to us  for a quotation . <br>" + "We are here to provide you with service at any time.<br>" + "<br>" + "Nice day and give my best wishes!",
            "Dear friends,<br>" + "Good day!<br>" + "I would like to thank you for your reading my letter. It is very pleased to communicate with you for the possibility of working together.<br>" + " <br>" + "Our company BOSUN Mould is a professional mould manufacturer,  has over 17 years¡¯ experience in plastic injection molds and Precise molds for plastic parts &metal parts.<br>" + " <br>" + "Good quality,competitive price and on time delivery .<br>" + "Some projects that need our assistance, please don't hesitate to contact me, we are here to provide you service at any time.<br>" + " <br>" + "Give my best wishes!"
    };

    private static final Map<String,String> SIGNATURES = new HashMap<String,String>(){
        {
            put("phyllis","<br><br>Sincerely,<br>" + "Phyllis Liu<br>" + "Overseas Marketing Dept.<br>" + "-------------------------------------------------------------------------------------------<br>" + "BOSUN Mould Technology Co.,LTD.<br>" + "Email:phyllis@bosun-mould.com<br>" + "Cell: 0086 18576459878  Skype:phyllis-Bosun Mould <br>" + "Tel: 0086-0755-28947686   Fax:0086-0755-28947976<br>" + "Add: #3-B,1 Road, Buxinji industrial, Guanjingtou, Fenggang, Dongguan city, Guangdong, China.");
            put("sara","<br><br>Sincerely,<br>" + "Sara Liu<br>" + "Overseas Marketing Dept.<br>" + "-------------------------------------------------------------------------------------------<br>" + "BOSUN Mould Technology Co.,LTD.<br>" + "Email:sara@bosun-mould.com<br>" + "Cell: 0086 18576459878  Skype:Sara-Bosun Mould <br>" + "Tel: 0086-0755-28947686   Fax:0086-0755-28947976<br>" + "Add: #3-B,1 Road, Buxinji industrial, Guanjingtou, Fenggang, Dongguan city, Guangdong, China.");
            put("sheena","<br><br>Sincerely,<br>" + "Sheena Li<br>" + "Overseas Marketing Dept.<br>" + "-------------------------------------------------------------------------------------------<br>" + "BOSUN Mould Technology Co.,LTD.<br>" + "Email:sheena@bosun-mould.com<br>" + "Cell: 0086 18576459878  Skype:Sheena-Bosun Mould <br>" + "Tel: 0086-0755-28947686   Fax:0086-0755-28947976<br>" + "Add: #3-B,1 Road, Buxinji industrial, Guanjingtou, Fenggang, Dongguan city, Guangdong, China.");
        }
    };

    private static final String[] USERS_CHOOSE ={
            "phyllis","sara","sheena"
    };

    private static final String[] LIST_SUBJECTS_NEW = {
            "Injection molds/molding/plastic parts"
            ,"Plastic Injection Mold / Die-Casting"
            ,"Plastic injection molds/molding/Die-cast Aluminum"
            ,"Injection molds/molding/molded products"
            ,"Injection molds/molding/Tool making"
            ,"Injection moulds/Moulding/plastic&metal parts"
            ,"Injection molds/Die Cast Dies/Moulding"
            ,"Injection moulds/moulding/Prototyping"
            ,"plastic injection moulds/Tooling/Dies"
            ,"Plastic injection moulds/Dies/tooling"
            ,"plastic injection moulds/moulding/plastic parts"
            ,"Injection moulds/Tooling/Dies"
            ,"Plastic Molds/injection molding/Tooling"
            ,"Injection molds/molding/Tools"
            ,"Plastic injection molds/Die Cast Dies/Moulding"
            ,"Injection molds/Dies/Tooling"
            ,"Plastic injection molds/molding/Tool making"
            ,"Plastic molds/moulding/Tools"
            ,"Injection molds/Die Cast Dies/Moulding"
    };

    public static String getBoay(String user){
        String signatures = SIGNATURES.get(user);
        int max = LIST_TEMPLATES_NEW.length;
        int min=0;
        Random random = new Random();
        int index = random.nextInt(max)%(max-min+1) + min;
        String body_1 =  LIST_TEMPLATES_NEW[index];
        String body = BODY_HEAD + body_1 + signatures + BODY_END;
        return body;
    }

    public static String getUser(){
        int max=USERS_CHOOSE.length;;
        int min=0;
        Random random = new Random();
        int index = random.nextInt(max)%(max-min+1) + min;
        String user = USERS_CHOOSE[index];
        return user;
    }

    public static String getSubject(){
        int max=LIST_SUBJECTS_NEW.length;;
        int min=0;
        Random random = new Random();
        int index = random.nextInt(max)%(max-min+1) + min;
        return LIST_SUBJECTS_NEW[index];
    }


}
