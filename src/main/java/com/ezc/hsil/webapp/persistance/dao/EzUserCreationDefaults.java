package com.ezc.hsil.webapp.persistance.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ezc.hsil.webapp.model.EzStateHead;
import com.ezc.hsil.webapp.model.EzStates;
import com.ezc.hsil.webapp.model.EzZonalHead;
import com.ezc.hsil.webapp.model.UserRoles;
import com.ezc.hsil.webapp.model.UserZones;

@Repository
public class EzUserCreationDefaults {
    private static final List<EzStates> STATES = new ArrayList<EzStates>();
    private static final List<EzStateHead> STATE_HEAD = new ArrayList<EzStateHead>();
    private static final List<EzZonalHead> ZONAL_HEAD = new ArrayList<EzZonalHead>();
    private static final List<UserRoles> ROLES = new ArrayList<UserRoles>();
    private static final List<UserZones> ZONES = new ArrayList<UserZones>();
	 
    static {
        initData();
    }
 
    private static void initData() {
    	
    	ROLES.add(new UserRoles("ADMIN", "Admin"));
    	ROLES.add(new UserRoles("ROLE_REQ_CR", "Sales Front Line"));
    	ROLES.add(new UserRoles("ROLE_ST_HEAD", "Sales State Head"));
    	ROLES.add(new UserRoles("ROLE_ZN_HEAD", "Sales Zonal Head"));
    	ROLES.add(new UserRoles("ROLE_IN_STOR", "In Store"));
    	ROLES.add(new UserRoles("ROLE_OUT_STOR", "Out Store"));

    	STATES.add(new EzStates("AN", "Andaman and Nicobar Islands","REQ_CR"));
    	STATES.add(new EzStates("AP", "Andhra Pradesh","REQ_CR"));
    	STATES.add(new EzStates("AR", "Arunachal Pradesh","REQ_CR"));
    	STATES.add(new EzStates("AS", "Assam","REQ_CR"));
    	STATES.add(new EzStates("BRP", "Bihar","REQ_CR"));
    	STATES.add(new EzStates("CH", "Chandigarh","REQ_CR"));
    	STATES.add(new EzStates("CT", "Chhattisgarh","REQ_CR"));
    	STATES.add(new EzStates("DN", "Dadra and Nagar Haveli","REQ_CR"));
    	STATES.add(new EzStates("DD", "Daman and Diu","REQ_CR"));
    	STATES.add(new EzStates("DL", "Delhi","REQ_CR"));
    	STATES.add(new EzStates("GA", "Goa","REQ_CR"));
    	STATES.add(new EzStates("GJ", "Gujarat","REQ_CR"));
    	STATES.add(new EzStates("HR", "Haryana","REQ_CR"));
    	STATES.add(new EzStates("HP", "Himachal Pradesh","REQ_CR"));
    	STATES.add(new EzStates("JK", "Jammu and Kashmir","REQ_CR"));
    	STATES.add(new EzStates("JH", "Jharkhand","REQ_CR"));
    	STATES.add(new EzStates("KA", "Karnataka","REQ_CR"));
    	STATES.add(new EzStates("KL", "Kerala","REQ_CR"));
    	STATES.add(new EzStates("LD", "Lakshadweep","REQ_CR"));
    	STATES.add(new EzStates("MP", "Madhya Pradesh","REQ_CR"));
    	STATES.add(new EzStates("MH", "Maharashtra","REQ_CR"));
    	STATES.add(new EzStates("MN", "Manipur","REQ_CR"));
    	STATES.add(new EzStates("ML", "Meghalaya","REQ_CR"));
    	STATES.add(new EzStates("MZ", "Mizoram","REQ_CR"));
    	STATES.add(new EzStates("NL", "Nagaland","REQ_CR"));
    	STATES.add(new EzStates("OR", "Odisha, Orissa","REQ_CR"));
    	STATES.add(new EzStates("PY", "Puducherry","REQ_CR"));
    	STATES.add(new EzStates("PB", "Punjab","REQ_CR"));
    	STATES.add(new EzStates("RJ", "Rajasthan","REQ_CR"));
    	STATES.add(new EzStates("SK", "Sikkim","REQ_CR"));
    	STATES.add(new EzStates("TN", "Tamil Nadu","REQ_CR"));
    	STATES.add(new EzStates("TG", "Telangana","REQ_CR"));
    	STATES.add(new EzStates("TR", "Tripura","REQ_CR"));
    	STATES.add(new EzStates("UP", "Uttar Pradesh","REQ_CR"));
    	STATES.add(new EzStates("UT", "Uttarakhand,Uttaranchal","REQ_CR"));
    	STATES.add(new EzStates("WB", "West Bengal","REQ_CR"));
    	
    	STATE_HEAD.add(new EzStateHead("AN_HD_GRP", "Head Andaman and Nicobar Islands","ST_HEAD"));
    	STATE_HEAD.add(new EzStateHead("AP_HD_GRP", "Head Andhra Pradesh","ST_HEAD"));	
    	STATE_HEAD.add(new EzStateHead("AR_HD_GRP", "Head Arunachal Pradesh","ST_HEAD"));
    	STATE_HEAD.add(new EzStateHead("AS_HD_GRP", "Head Assam","ST_HEAD"));		
    	STATE_HEAD.add(new EzStateHead("BR_HD_GRP", "Head Bihar","ST_HEAD"));		
    	STATE_HEAD.add(new EzStateHead("CH_HD_GRP", "Head Chandigarh","ST_HEAD"));		
    	STATE_HEAD.add(new EzStateHead("CT_HD_GRP", "Head Chhattisgarh","ST_HEAD"));	
    	STATE_HEAD.add(new EzStateHead("DN_HD_GRP", "Head Dadra and Nagar Haveli","ST_HEAD"));	
    	STATE_HEAD.add(new EzStateHead("DD_HD_GRP", "Head Daman and Diu","ST_HEAD"));
    	STATE_HEAD.add(new EzStateHead("DL_HD_GRP", "Head Delhi","ST_HEAD"));	
    	STATE_HEAD.add(new EzStateHead("GA_HD_GRP", "Head Goa","ST_HEAD"));		
    	STATE_HEAD.add(new EzStateHead("GJ_HD_GRP", "Head Gujarat","ST_HEAD"));		
    	STATE_HEAD.add(new EzStateHead("HR_HD_GRP", "Head Haryana","ST_HEAD"));			
    	STATE_HEAD.add(new EzStateHead("HP_HD_GRP", "Head Himachal Pradesh","ST_HEAD"));	
    	STATE_HEAD.add(new EzStateHead("JK_HD_GRP", "Head Jammu and Kashmir","ST_HEAD"));
    	STATE_HEAD.add(new EzStateHead("JH_HD_GRP", "Head Jharkhand","ST_HEAD"));		
    	STATE_HEAD.add(new EzStateHead("KA_HD_GRP", "Head Karnataka","ST_HEAD"));		
    	STATE_HEAD.add(new EzStateHead("KL_HD_GRP", "Head Kerala","ST_HEAD"));		
    	STATE_HEAD.add(new EzStateHead("LD_HD_GRP", "Head Lakshadweep","ST_HEAD"));	
    	STATE_HEAD.add(new EzStateHead("MP_HD_GRP", "Head Madhya Pradesh","ST_HEAD"));	
    	STATE_HEAD.add(new EzStateHead("MH_HD_GRP", "Head Maharashtra","ST_HEAD"));	
    	STATE_HEAD.add(new EzStateHead("MN_HD_GRP", "Head Manipur","ST_HEAD"));		
    	STATE_HEAD.add(new EzStateHead("ML_HD_GRP", "Head Meghalaya","ST_HEAD"));	
    	STATE_HEAD.add(new EzStateHead("MZ_HD_GRP", "Head Mizoram","ST_HEAD"));		
    	STATE_HEAD.add(new EzStateHead("NL_HD_GRP", "Head Nagaland","ST_HEAD"));	
    	STATE_HEAD.add(new EzStateHead("OR_HD_GRP", "Head Odisha, Orissa","ST_HEAD"));	
    	STATE_HEAD.add(new EzStateHead("PY_HD_GRP", "Head Puducherry","ST_HEAD"));		
    	STATE_HEAD.add(new EzStateHead("PB_HD_GRP", "Head Punjab","ST_HEAD"));		
    	STATE_HEAD.add(new EzStateHead("RJ_HD_GRP", "Head Rajasthan","ST_HEAD"));		
    	STATE_HEAD.add(new EzStateHead("SK_HD_GRP", "Head Sikkim","ST_HEAD"));			
    	STATE_HEAD.add(new EzStateHead("TN_HD_GRP", "Head Tamil Nadu","ST_HEAD"));			
    	STATE_HEAD.add(new EzStateHead("TG_HD_GRP", "Head Telangana","ST_HEAD"));	
    	STATE_HEAD.add(new EzStateHead("TR_HD_GRP", "Head Tripura","ST_HEAD"));		
    	STATE_HEAD.add(new EzStateHead("UP_HD_GRP", "Head Uttar Pradesh","ST_HEAD"));
    	STATE_HEAD.add(new EzStateHead("UT_HD_GRP", "Head Uttarakhand,Uttaranchal","ST_HEAD"));
    	STATE_HEAD.add(new EzStateHead("WB_HD_GRP", "Head West Bengal","ST_HEAD"));	
    	
    	ZONAL_HEAD.add(new EzZonalHead("EAST_ZONE_GRP", "Head East Zone","ZN_HEAD"));		
    	ZONAL_HEAD.add(new EzZonalHead("WEST_ZONE_GRP", "Head West Zone","ZN_HEAD"));		
    	ZONAL_HEAD.add(new EzZonalHead("NORTH_ZONE_GRP", "Head North Zone","ZN_HEAD"));		
    	ZONAL_HEAD.add(new EzZonalHead("SOUTH_ZONE_GRP", "Head South Zone","ZN_HEAD"));		
    	
    	ZONES.add(new UserZones("EAST", "East Zone"));
    	ZONES.add(new UserZones("WEST", "West Zone"));
    	ZONES.add(new UserZones("NORTH", "North Zone"));
    	ZONES.add(new UserZones("SOUTH", "South Zone"));
    }
 
    public List<EzStates> getStates() {
        return STATES;
    }
 
    public Map<String, String> getMapCountries() {
        Map<String, String> map = new HashMap<String, String>();
        for (EzStates c : STATES) {
            map.put(c.getCode(), c.getDesc());
        }
        return map;
    }
    
    public List<EzStateHead> getST_HDGroups() {
        return STATE_HEAD;
    }
    
    public List<EzZonalHead> getZN_HDGroups() {
        return ZONAL_HEAD;
    }
    
    public List<UserRoles> getRoles() {
        return ROLES;
    }
 
    public Map<String, String> getMapRoles() {
        Map<String, String> map = new HashMap<String, String>();
        for (UserRoles c : ROLES) {
            map.put(c.getRoleId(), c.getRoleDesc());
        }
        return map;
    }
    public List<UserZones> getZones() {
        return ZONES;
    }
 
    public Map<String, String> getMapZones() {
        Map<String, String> map = new HashMap<String, String>();
        for (UserZones c : ZONES) {
            map.put(c.getZonalId(), c.getZonalName());
        }
        return map;
    }
    
}
