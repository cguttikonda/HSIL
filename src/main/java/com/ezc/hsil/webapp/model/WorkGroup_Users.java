package com.ezc.hsil.webapp.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;



@Entity
@Table(name="EZC_WORKGROUP_USERS")
public class WorkGroup_Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

   
    @Column(name="EGU_ID")
	private String groupId;
	
    @Column(name="EGU_USER_ID")
   	private String userId;
    
    @Column(name="EGU_STATE_GRP")
   	private String stateGrp;

    @Column(name="EGU_ZONAL_GRP")
   	private String zonalGrp;
    
    @Column(name="EGU_EXT1")
   	private String ext1;
    
    @Column(name="EGU_EXT2")
   	private String ext2;

    protected WorkGroup_Users() {

    }

    public WorkGroup_Users(final String groupId,final String userId,final String stateGrp,final String zonalGrp,final String ext1,final String ext2) {
        super();
        this.groupId = groupId;
        this.userId = userId;
        this.stateGrp = stateGrp;
        this.zonalGrp = zonalGrp;
        this.ext1 = ext1;
        this.ext2 = ext2;
    }
 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStateGrp() {
		return stateGrp;
	}

	public void setStateGrp(String stateGrp) {
		this.stateGrp = stateGrp;
	}

	public String getZonalGrp() {
		return zonalGrp;
	}

	public void setZonalGrp(String zonalGrp) {
		this.zonalGrp = zonalGrp;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WorkGroup_Users groupUsers = (WorkGroup_Users) obj;
        if (!userId.equals(groupUsers.userId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        //builder.append("Role [name=").append(name).append("]").append("[id=").append(id).append("]");
        return builder.toString();
    }
}
