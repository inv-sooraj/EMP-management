package com.project.employee.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.project.employee.form.UserDetailForm;

@Entity
public class User {
	public static enum Status {
        INACTIVE((byte) 0),
        ACTIVE((byte) 1);

        public final byte value;
        private Status(byte value) {
            this.value = value;
        }
    }
	public static enum Role {
        ADMIN((byte) 0),
        EMPLOYEE((byte) 1),
        EMPLOYER((byte) 2);
        

        public final byte value;

        private Role(byte value) {
            this.value = value;
        }
    }
	public static enum Qualification {
        TENTH((byte) 0),
        TWELFTH((byte) 1),
        UG((byte) 2),
        PG((byte) 3);	

        public final byte value;

        private Qualification(byte value) {
            this.value = value;
        }
    }
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
	private String userName;
	private String password;
	private String email;
	private String name;
	private String address;
	private String phoneNo;
	private byte role;
	private byte qualification;
	private byte status;
	@Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    
    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    	
    public User(String userName,String email,String password,byte role) {
        this.userName = userName;
        this.password = password;
        this.email =email;
        this.role=role;
        this.status = Status.ACTIVE.value;
        Date dt = new Date();	
        this.createDate = dt;
        this.updateDate = dt;
    }
    
    
    public User update(UserDetailForm form) {
       
        this.name = form.getName();
        this.address=form.getAddress();
        this.phoneNo=form.getPhoneNo();
        this.qualification=form.getQualification();
        Date dt = new Date();
        this.updateDate = dt;
        return this;
    }
    
    public User delete() {

        this.status = User     .Status.INACTIVE.value;

        this.updateDate = new Date();
        return this;
    }

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public byte getRole() {
		return role;
	}

	public byte getQualification() {
		return qualification;
	}

	public void setRole(byte role) {
		this.role = role;
	}

	public void setQualification(byte qualification) {
		this.qualification = qualification;
	}
	public byte getStatus() {
			return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
    
    

}
