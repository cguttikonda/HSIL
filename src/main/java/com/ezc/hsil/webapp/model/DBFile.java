package com.ezc.hsil.webapp.model;

import org.hibernate.annotations.GenericGenerator;

import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "EZC_UPLOAD_FILES")
public class DBFile {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
   
    private int id;
 
    @Column(name = "EUF_REQ_ID")
    
    private String reqID;
    
    @Column(name="EUF_FILENAME",length=500)
    private String fileName;

    @Column(name="EUF_FILETYPE",length=20)
    private String fileType;
    
    @Column(name="EUF_FILEPATH",length=200)
    private String filePath;

    @Lob 
    @Column(name="EUF_DATA",length=1000)
    private byte[] data;

    public DBFile() {
 
    }

    public DBFile(String fileName, String fileType,  String filePath) {
        this.fileName = fileName; 
        this.fileType = fileType;
        this.filePath = filePath;
    }

    public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

    public String getReqID() {
		return reqID;
	}

	
	public void setReqID(String reqID) {
		this.reqID = reqID;
	}

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

    // Getters and Setters (Omitted for brevity)
}