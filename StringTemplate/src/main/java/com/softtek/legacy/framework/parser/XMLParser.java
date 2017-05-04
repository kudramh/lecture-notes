package com.softtek.legacy.framework.parser;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.stringtemplate.v4.*;

import java.util.Arrays;

public class XMLParser {
    Logger log = LoggerFactory.getLogger(XMLParser.class);

    private String  fileName;
    private Element serviceElement = null;
    private List<Map<String, String>> headerFields;
    private List<Map<String, String>> inputFields;
    private List<Map<String, String>> outputFields;
    private List<Map<String, String>> errorFields;

    public XMLParser(String strFileName) {
        this.fileName = strFileName;
        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new FileInputStream(fileName));
            serviceElement = document.getDocumentElement();
        } catch(Exception ex) {
            log.error("Error when parsing XML File: "+fileName);
        }
    }

    public void parse() throws Exception {
        try {
            headerFields = this.parseFieldAttributes("header-fields");
            inputFields  = this.parseFieldAttributes("input-fields");
            outputFields = this.parseFieldAttributes("output-fields");
//          errorFields  = this.parseFieldAttributes("error-fields");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, String>> parseFieldAttributes(String fieldGroup) {
        List<Map<String,String>> fieldsList = new ArrayList<Map<String,String>>();
        NodeList headerList = serviceElement.getElementsByTagName(fieldGroup);
        Node headerNode = headerList.item(0);
        NodeList headerFields = headerNode.getChildNodes();
        for(int indx=0; indx < headerFields.getLength(); indx++) {
            Node fieldNode = headerFields.item(indx);
            if(fieldNode.getNodeType() == Node.ELEMENT_NODE){
                Map fieldsMap = new HashMap<String,String>();
                NamedNodeMap fieldAttr = fieldNode.getAttributes();
                fieldsMap.put("description", fieldAttr.getNamedItem("description").getNodeValue());
                String bufferName = "";
                if (fieldAttr.getNamedItem("buffer-name").getNodeValue().length() > 0){
                    bufferName = Arrays.stream( fieldAttr.getNamedItem("buffer-name").getNodeValue().split("_" ) )
                                       .map(s ->{return Character.toUpperCase(s.charAt(0))+ s.toLowerCase().substring(1);})
                                       .reduce("", String::concat);
                    fieldsMap.put("fieldName",  Character.toLowerCase(bufferName.charAt(0))+bufferName.substring(1) );
                }
                fieldsMap.put("fieldType",  fieldAttr.getNamedItem("type").getNodeValue().toUpperCase());
                fieldsMap.put("methodName", bufferName);
                fieldsMap.put("methodType", fieldAttr.getNamedItem("type").getNodeValue());
                fieldsMap.put("length",     fieldAttr.getNamedItem("length").getNodeValue());
                fieldsMap.put("required",   fieldAttr.getNamedItem("required").getNodeValue().toString().equalsIgnoreCase("true"));
                fieldsList.add(fieldsMap);
            }
        }
        return fieldsList;
    }

    public List<Map<String,String>> getHeaderFields(){ return headerFields;}
    public List<Map<String,String>> getInputFields() { return inputFields;}
    public List<Map<String,String>> getOutputFields(){ return outputFields;}
    public List<Map<String,String>> getErrorFields() { return errorFields;}

    public static void main(String[] args) throws Exception {
        XMLParser xmlParser = new XMLParser("c:/Users/kudra/Downloads/obpm-service.xml");
        xmlParser.parse();
        try {
            STGroup group = new STGroupDir("c:/Temp");
            ST st = group.getInstanceOf("XmlModel");
            st.add("class", "Test");
            st.add("packageName", "com.softtek.legacy.framework");
            List<Map<String,String>> headerList = xmlParser.getHeaderFields();
            st.add("fields", headerList);
            System.out.println(st.render());
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }


}
