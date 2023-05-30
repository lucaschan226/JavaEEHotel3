package com.bcu.homeroom.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


import com.bcu.homeroom.bean.Band;
import com.bcu.homeroom.bean.Hotel;
import com.bcu.homeroom.service.BandService;
import com.bcu.homeroom.util.JsonDateValueProcessor;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

@WebServlet("/BandServlet")
public class BandServlet extends HttpServlet {	
	private BandService bandService = new BandService();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		String cmd = request.getParameter("cmd");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/json;charset=UTF8");

        PrintWriter out=response.getWriter();
		List<Band> bands = new ArrayList<Band>();
		List<Band> bandone = new ArrayList<Band>();
		if("all".equals(cmd))
		{
			try {
				bands=bandService.getAllBand();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JsonConfig jsonConfig = new JsonConfig();   //JsonConfig��net.sf.json.JsonConfig�е������Ϊ�̶�д��  
			jsonConfig.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());    
			JSONArray Nbands = JSONArray.fromObject(bands, jsonConfig); 
			out.println(Nbands);
		}
		if("findByCondition".equals(cmd))
		{
			int bandid=Integer.parseInt(request.getParameter("bandid"));
			String bandname=request.getParameter("bandname");
			try {
				bands=bandService.getConditionBand(bandid,bandname);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JsonConfig jsonConfig = new JsonConfig();   //JsonConfig��net.sf.json.JsonConfig�е������Ϊ�̶�д��  
			jsonConfig.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());    
			JSONArray Nbands = JSONArray.fromObject(bands, jsonConfig); 
			out.println(Nbands);
		}

		if("del".equals(cmd))
		{
			String bandid=request.getParameter("bandid");
			boolean IsDel=bandService.deleteBand(Integer.parseInt(bandid));
			out.println(IsDel);
		}
		if("find".equals(cmd))
		{
			String bandid=request.getParameter("bandid");
			try {
				bandone=bandService.getOneBand(Integer.parseInt(bandid));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			JsonConfig jsonConfig = new JsonConfig();   //JsonConfig��net.sf.json.JsonConfig�е������Ϊ�̶�д��  
			jsonConfig.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());    
			JSONArray NhotelOne = JSONArray.fromObject(bandone, jsonConfig); 
			System.out.println(NhotelOne);
			out.println(NhotelOne);
		}
		
	}
private void CreateFolider(String realPath,String tempPath)
{
    File f = new File(realPath);
    if (!f.exists() && !f.isDirectory()) {
        System.out.println("Ŀ¼���ļ�������! ����Ŀ��Ŀ¼��");
        f.mkdir();
    }

}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String cmd=request.getParameter("cmd");
         System.out.println(cmd);
		try {   
		//�õ��ϴ��ļ��ı���Ŀ¼�� ���ϴ����ļ������WEB-INFĿ¼�£����������ֱ�ӷ��ʣ���֤�ϴ��ļ��İ�ȫ
            String realPath="";//  /WEB-INF/files
            String fileName="";
            //������ʱĿ¼�� �ϴ��ļ����ڻ��������ȷ�����ʱĿ¼��
            // String tempPath = this.getServletContext().getRealPath("images/Band");
            String tempPath = "D:\\2019-2020-2\\Home_Server\\WebContent\\images\\Band";
            //�жϴ���ϴ��ļ���Ŀ¼�Ƿ���ڣ��������򴴽���
            //�ж���ʱĿ¼�Ƿ���ڣ��������򴴽���
            /**
             * ʹ��Apache�ļ��ϴ���������ļ��ϴ����裺
             *
             * */
            //1�����û���:����һ��DiskFileItemFactory����
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //�����ϴ��ļ�����ʱĿ¼
            System.out.println(tempPath);
            File f1 = new File(tempPath);
            if (!f1.isDirectory()) {
                System.out.println("��ʱ�ļ�Ŀ¼������! ������ʱ�ļ�Ŀ¼");
                f1.mkdir();
            }
            factory.setRepository(f1);

            //2�����Ĳ�����:����һ���ļ��ϴ���������
            ServletFileUpload upload = new ServletFileUpload(factory);
            //����ϴ�"�ļ���"����������
            upload.setHeaderEncoding("UTF-8");

            //3���ж�enctype:�ж��ύ�����������Ƿ����ϴ���������
            if (!ServletFileUpload.isMultipartContent(request)) {
                System.out.println("�����ϴ��ļ�����ֹ");
                //���մ�ͳ��ʽ��ȡ����
                return;
            }

            //==��ȡ������==
           //���Ƶ����ϴ��ļ���С(5M)
           upload.setFileSizeMax(1024*1024*4);
            //�������ϴ��ļ���С(10M)
           upload.setSizeMax(1024*1024*6);
           
            /**
             * ���и��Ĳ��֣�
             *  ʹ��HashMap�� �����ļ����ݽ��д洢��
             *  ʹ�ù���
             *  һ��Ҫ����Ŀid����ֵ�����ļ�����
             *  �ļ��Ĵ����ļ�����������Ŀid�������������޷����棡
             *
             */
            HashMap<String,String> info =new HashMap<String, String>();

            //4��ʹ��ServletFileUpload�����������ϴ����ݣ�����������ص���һ��List<FileItem>���ϣ�ÿһ��FileItem��Ӧһ��Form����������
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                //���fileitem�з�װ������ͨ����������ݣ��������ֵ��
                if (item.isFormField()) {
                    String filedName = item.getFieldName();//��ͨ���������ݵ���
                    //�����ͨ����������ݵ�������������
                    String filedValue = item.getString("UTF-8");//��ͨ�������ֵ
                    System.out.println("��ͨ�ֶ�:" + filedName + "==" + filedValue);
                    info.put(filedName,filedValue);
                    /**
                     * �� �� HashMap�д�ֵ����
                     */
                } else {
                    //���fileitem�з�װ�����ϴ��ļ����õ��ϴ����ļ����ƣ�
                    String filedName = item.getFieldName();
                    //realPath=this.getServletContext().getRealPath("images/Band/");
                    realPath="D:\\2019-2020-2\\Home_Server\\WebContent\\images\\Band\\";
                    CreateFolider(realPath,tempPath);
                    fileName = item.getName();
                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                    info.put("bicon",fileName);
                    System.out.println("�ļ�����"+fileName);
                    
                    /**
                     * String fileName = item.getName();//�ϴ��ļ�����
                     */

                    //����ļ��ϴ�������п� �� �쳣����
                    if (fileName == null || "".equals(fileName.trim())) {  //ȥ�ո��Ƿ�Ϊ��
                        continue;// Ϊ�գ���������ѭ����  ��һ��û�������������Լ�������ڶ���
                    }

                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                    //ƴ���ϴ�·�������·��+�ϴ����ļ���
                    String filePath = realPath + "\\" + fileName;
                    System.out.println("��������--"+filePath);
                    //�������������
                    InputStream in = item.getInputStream(); //��ȡitem�е��ϴ��ļ���������
                    OutputStream fout = new FileOutputStream(filePath); //����һ���ļ������

                    //����һ��������
                    byte b[] = new byte[1024];
                    //�ж��������е������Ƿ��Ѿ�����ı�ʶ
                    int len = -1;
                    //ѭ�������������뵽���������У�(len=in.read(buffer))��=-1�ͱ�ʾin���滹������
                    while ((len = in.read(b)) != -1) {  //û�����˷���-1
                        //ʹ��FileOutputStream�������������������д�뵽ָ����Ŀ¼(savePath+"\\"+filename)����
                        fout.write(b, 0, len);
                    }
                    //�ر���
                    fout.close();
                    in.close();
                    //ɾ����ʱ�ļ�
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    item.delete();//ɾ�������ļ��ϴ�ʱ���ɵ���ʱ�ļ�
                    System.out.println("�ļ��ϴ��ɹ�");

                }
            }
            System.out.println(JSONArray.fromObject(info));
            boolean IsDone=false;
            if("add".equals(cmd)) {
            	IsDone=bandService.insertBand(-1,info.get("bname"),"images\\Band\\"+info.get("bicon"),info.get("bDetail"),info.get("bRemark"));
            }
            else
            {
            	IsDone=bandService.editBand(Integer.parseInt(info.get("bId")),info.get("bname"),"images\\Band\\"+info.get("bicon"),info.get("bDetail"),info.get("bRemark"));
            }
            PrintWriter out=response.getWriter();
			out.println(IsDone);
		
		} catch (FileUploadException e) {
            e.printStackTrace();
        }
    
	}
}
