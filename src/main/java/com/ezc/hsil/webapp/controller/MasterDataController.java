package com.ezc.hsil.webapp.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezc.hsil.webapp.dto.DistributorDto;
import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.MaterialDto;
import com.ezc.hsil.webapp.dto.PlaceMasterDto;
import com.ezc.hsil.webapp.dto.StockLocationDto;
import com.ezc.hsil.webapp.dto.StockTransferDto;
import com.ezc.hsil.webapp.dto.UserGroupAddDto;
import com.ezc.hsil.webapp.dto.UserGroupDto;
import com.ezc.hsil.webapp.model.DistributorMaster;
import com.ezc.hsil.webapp.model.EzStores;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.model.Work_Groups;
import com.ezc.hsil.webapp.service.IMasterService;
import com.ezc.hsil.webapp.service.IUserService;
import com.ezc.hsil.webapp.util.EzExcelUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/master")
public class MasterDataController {

	@Autowired
    private EzExcelUtil ezExcelUtil;

	@Autowired
	IMasterService masterService;
	
	@Autowired
    private IUserService iUserService;

	@Value("#{'${city}'.split(',')}")
	private List<String> city;
	
	
	@GetMapping("/addDis")
	public String showDistributorForm(Model model) {

	
		log.info("In the class {}", this.getClass());
		
		DistributorDto disDto = new DistributorDto();
		
		model.addAttribute("city", city);
		model.addAttribute("distributorDto", disDto);
		
		return "master/addDistributor";
	}
	
	
	@PostMapping("/add")
	public String saveDistData(@Valid @ModelAttribute("distributorDto") DistributorDto disDto, final BindingResult bindingResult,RedirectAttributes model) {
	
		if(bindingResult.hasErrors()) {
		
			log.info("Binding Results:::: {}", bindingResult);
			return "master/addDistributor";
		}
		else {
		
			log.info("In the class {}", disDto.toString());
		
			model.addFlashAttribute("success", "Distributor added successfully.");
			masterService.addNewDistributor(disDto);
			return "redirect:/master/addDis";
		}
	}
	
	
	@GetMapping("/listDis")
	public String listDistributors(Model model) {
		
		model.addAttribute("distList", masterService.findAll());

		return "master/distributorList";
	}

	@PostMapping("/edit-dist")
	public String updateDistributor(@Valid @ModelAttribute("distributorDto") DistributorDto disDto,
			final BindingResult bindingResult) throws SQLException {
		
		if(bindingResult.hasErrors()) {
			
			log.info("Binding Results:::: {}", bindingResult);
			return "master/distributorList :: edit-dist"; 
		}
		else {
		
			log.info("In the class {}", disDto.toString());
		
			masterService.updateDistributor(disDto);
			return "redirect:/master/listDis";
		}
		
	}
	@PostMapping("/delete-dist/{code}")
	public String deleteDistributor(@PathVariable("code") String code) {
			log.debug("in controller:::code::::::"+code);		 
			masterService.deleteDistributor(code);
			return "redirect:/master/listDis";
	}
	
	@GetMapping("/addMaterial")
	public String showMaterialForm(Model model) {
		
		List<EzStores> storeList=masterService.listStores();
		model.addAttribute("storeList",storeList);
		model.addAttribute("materialDto", new MaterialDto());
		return "master/addMaterial"; //html
	}
	@PostMapping("/addMaterial")
	public String saveMaterail(@Valid @ModelAttribute("materialDto") MaterialDto mDto, 
			BindingResult bindingResult, RedirectAttributes ra, Model model,@RequestParam(value = "stkAva", required = false) int stkAva) throws SQLException {
		
		if(bindingResult.hasErrors()) {
			
			log.info("bindingResult:::::of material {}",bindingResult);
			return "master/addMaterial";
		}else {
			String matCode=mDto.getMaterialCode();
			log.debug("stkAva"+stkAva);
			int qty=mDto.getQuantity();
			int totQty=qty+stkAva;
			log.debug("totQty"+totQty);
			boolean isPresent = masterService.findById(matCode,mDto.getStockLoc());
			log.debug("isPresent"+isPresent);
			if(isPresent)
			{
				mDto.setIsActive("Y");
				mDto.setQuantity(totQty);
				mDto.setModifiedON(new Date());
				ra.addFlashAttribute("success", "Material Updated successfully.");
				masterService.updateMaterial(mDto);
				
			}
			else
			{	
			ra.addFlashAttribute("success", "Material added successfully.");
			masterService.addNewMaterial(mDto);
			}
		}
		
		return "redirect:/master/addMaterial"; //html
	}

	@GetMapping("/listMaterial") 
	public String listMaterials(Model model) {
		
		model.addAttribute("matList", masterService.getAllMaterials());

		return "master/materialList";
	}
	@PostMapping("/edit-material" )
	public String updateMaterial(@Valid @ModelAttribute("materialDto") MaterialDto matDto,
			final BindingResult bindingResult) throws SQLException {
		
		if(bindingResult.hasErrors()) {
			
			log.info("Binding Results::updateMaterial:: {}", bindingResult);
			return "master/listMaterial :: edit-mat"; 
		}
		else {
		
			log.info("In the class {}", matDto.toString());
		
			masterService.updateMaterial(matDto);
			return "redirect:/master/listMaterial";
		}
		
	}
	@PostMapping("/delete-mat/{id}/{stockLoc}")
	public String deleteMaterial(@PathVariable String id,@PathVariable String stockLoc) {
		log.debug("code"+id);
			masterService.deleteMaterial(id,stockLoc);
			return "redirect:/master/listMaterial";
	}
	@GetMapping("/addCity") 
	public String showAddCity(Model model) {
		
		model.addAttribute("placeMasterDto",new PlaceMasterDto());

		return "master/addCity";
	}
	@PostMapping("/addCity")
	public String saveCity(@Valid @ModelAttribute("placeMasterDto") PlaceMasterDto pDto, 
			BindingResult bindingResult, RedirectAttributes ra, Model model)  {
		log.debug("saving city");
		if(bindingResult.hasErrors()) {
			
			log.info("bindingResult:::::of City {}",bindingResult);
			return "master/addCity";
		}else {
			ra.addFlashAttribute("success", "City added successfully.");
			masterService.addNewCity(pDto);
		
		}
		
		return "redirect:/master/addCity"; 
	}
	@PostMapping("/delete-city/{id}")
	public String deleteCity(@PathVariable int id) {
		log.debug("code"+id);
			masterService.deleteCity(id);
			return "redirect:/master/listCity";
	}
	@GetMapping("/listCity") 
	public String listCity(Model model) {
		
		model.addAttribute("cityList", masterService.findAllCities());

		return "master/cityList";
	}
	@RequestMapping(value = "/distSamp", method = RequestMethod.GET)
    public StreamingResponseBody getDistSample(HttpServletResponse response) throws IOException {

        //response.setContentType("application/vnd.ms-excel"); 
        //response.setHeader("Content-Disposition", "attachment; filename=\"distributors.xls\"");
		response.setContentType("application/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); 
        response.setHeader("Content-Disposition", "attachment; filename=\"distributors.xlsx\"");
        String [] distCodeArr={"Distributor Code","Distributor Name","Contact No","State","City","Type"};
        return ezExcelUtil.writeExcel(distCodeArr,null,"distributors"); 
    }
	
	@RequestMapping(value = "/matSamp", method = RequestMethod.GET)
    public StreamingResponseBody getMastSample(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel"); 
        response.setHeader("Content-Disposition", "attachment; filename=\"material.xls\"");
        String [] matArr={"Material Code","Material Desc","Store","Quantity"};
        return ezExcelUtil.writeExcel(matArr,null,"material"); 
    }
	@RequestMapping(value = "/distDown", method = RequestMethod.GET)
    public StreamingResponseBody getDistDownload(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel"); 
        response.setHeader("Content-Disposition", "attachment; filename=\"distributors.xls\"");
        String [] distCodeArr={"Distributor Code","Distributor Name","Contact No","State","City","Type"};

        List<DistributorMaster> distMastList= masterService.findAll();
        List<Object[]> objArrList=null;
		try {
			objArrList = getObjectArray(distMastList,6);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return ezExcelUtil.writeExcel(distCodeArr,objArrList,"distributors"); 
    }
	@RequestMapping(value = "/matDown", method = RequestMethod.GET)
    public StreamingResponseBody getMatDownload(HttpServletResponse response) throws IOException {

        //response.setContentType("application/vnd.ms-excel"); 
        //response.setHeader("Content-Disposition", "attachment; filename=\"material.xlsx\"");
		response.setContentType("application/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); 
        response.setHeader("Content-Disposition", "attachment; filename=\"material.xlsx\"");
        String [] matCodeArr={"Material Code","Material Name","Store","Quantity"};

        List<MaterialMaster> matMastList= masterService.findMatAll();
        List<Object[]> objArrList=new ArrayList<Object[]>();
      //objArrList = getObjectArray(matMastList,3);
        if(matMastList != null)
        {
        	for(MaterialMaster matObj:matMastList)
        	{
        		Object[] tempArr=new Object[4];
        		tempArr[0]=matObj.getMaterialCode();
        		tempArr[1]=matObj.getMaterialDesc();
        		tempArr[2]=matObj.getStockLoc();
        		tempArr[3]=matObj.getQuantity();
        		objArrList.add(tempArr);
        	}
        }
        return ezExcelUtil.writeExcel(matCodeArr,objArrList,"materials"); 
    }

	@PostMapping("/distUpload")
	public String uploadDistributor(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {
		List<DistributorMaster> distList = new ArrayList<DistributorMaster>();
		List<Object[]> lisObjArr= ezExcelUtil.readExcel(reapExcelDataFile.getInputStream(),reapExcelDataFile.getOriginalFilename());
		 for(Object[] objArr:lisObjArr)
		 {
				 DistributorMaster distMaster = new DistributorMaster();
				 distMaster.setCode((String)objArr[0]);
				 distMaster.setName((String)objArr[1]);
				 distMaster.setContact((String)objArr[2]);
				 distMaster.setOrganisation((String)objArr[3]);
				 distMaster.setCity((String)objArr[4]);
				 distMaster.setType((String)objArr[5]);
				 distList.add(distMaster);
		 }
		 masterService.addDistributorMultiple(distList);
		 return "redirect:/master/listDis";
	    }
	@PostMapping("/matUpload")
	public String uploadMaterial(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {
		List<MaterialMaster> matList = new ArrayList<MaterialMaster>();
		List<Object[]> lisObjArr= ezExcelUtil.readExcel(reapExcelDataFile.getInputStream(),reapExcelDataFile.getOriginalFilename());
		 for(Object[] objArr:lisObjArr)
		 {
			 	 MaterialMaster matMaster = new MaterialMaster();
			 	 String quan=(String)objArr[3];
			 	int qty=0;
			 	try {
			 		qty = Integer.parseInt(quan);

			 	} catch(NumberFormatException e) {
			 	    double d = Double.parseDouble(quan); 
			 	   qty = (int) d;
			 	   log.debug("Error after parsing qty:::"+qty);
			 	}
			 	 matMaster.setMaterialCode((String)objArr[0]);
			 	 matMaster.setMaterialDesc((String)objArr[1]);
			 	 matMaster.setStockLoc((String)objArr[2]);
			 	 matMaster.setQuantity(qty);
			 	 matMaster.setIsActive("Y");
			 	 matMaster.setModifiedON(new Date());
			 	 matMaster.setCreatedON(new Date());
			 	 matList.add(matMaster);
		 }
		 masterService.addMaterialMultiple(matList);
		 return "redirect:/master/listMaterial";
	    }
	
	
	@RequestMapping(value = "/checkStock/{material}/{stockLoc}/{qty}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,String> checkMaterialStock( @PathVariable String material,@PathVariable String stockLoc, @PathVariable int qty) {
		
		return masterService.checkMaterialStock(material,stockLoc,qty);
	}

	
	public <T extends Object> List<Object[]> getObjectArray(List<T> objList,int length) throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException {
		   
		   List<Object[]> listOutArr = new ArrayList<Object[]>();
		   for(T obj:objList)
		   {
			   Field[] fields = obj.getClass().getDeclaredFields();
			   int fieldCount = fields.length;
			   Object[] objArr = new Object[fieldCount];
	
			   for (int i = 0; i < length; i++) {
			     Field field = fields[i];
			     field.setAccessible(true);
	
			     objArr[i] = field.get(obj);
			   }
			   listOutArr.add(objArr);
		   }
		   return listOutArr;
		}
	@RequestMapping(value="/getExistingQuantity/{matCode}/{stockLoc}",method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody  MaterialDto getMatDetails(@PathVariable String matCode,@PathVariable String stockLoc)
	{
		MaterialDto matdto=masterService.getMaterialDetails(matCode,stockLoc);
		Integer existingQty=0;
		
		String matDesc="";
		try {
		existingQty=matdto.getQuantity();
		matDesc=matdto.getMaterialDesc();
		}catch(Exception e)
		{
			existingQty=0;
		}
		
		log.debug("existingQty"+existingQty);
		log.debug("matDesc"+matDesc);
		log.debug("modON"+matdto.getModifiedON());
		return matdto;
	}
	@RequestMapping(value="/getExistingCity/{city}",method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody int getExistingCity(@PathVariable String city)
	{
		PlaceMasterDto pdto=masterService.getCityDetails(city);
		int id;
		try {
			id=pdto.getId();
		}catch(Exception e)
		{
			id=0;
		}
		log.debug("response"+id);
		return id;
	}

	@GetMapping("/addStore")
	public String showStocKLocationForm(Model model) {
		
		model.addAttribute("stockLocDto", new StockLocationDto());
		return "master/addStockLocation"; //html
	}
	
	
	
	@PostMapping("/saveStockLocation")
	public String saveMaterail(@Valid @ModelAttribute("stockLocDto") StockLocationDto stockLocDto, 
			BindingResult bindingResult, RedirectAttributes ra, Model model) throws SQLException {
		
		
			String locationId=stockLocDto.getLocationId();
			log.debug("locationId"+locationId);
			
			EzStores storeObj = masterService.findStoreById(locationId);
			if(storeObj == null)
			{
				masterService.addStore(stockLocDto);
				ra.addFlashAttribute("success", "Store Details Saved successfully.");
				
				
			}
			else
			{	
				ra.addFlashAttribute("error", "Store Details "+storeObj.getLocationId()+"already exists");
			}
		
		return "redirect:/master/addStore"; //html
	}
	@GetMapping("/listStores") 
	public String listStores(Model model) {
		
		model.addAttribute("storeList", masterService.listStores());

		return "master/listStore";
	}
	
	@GetMapping("/showTransferStock")
	public String transferStock(Model model) {
		
		List<EzStores> storeList=masterService.listStores();
		model.addAttribute("storeList",storeList);
		model.addAttribute("stockTransferDto", new StockTransferDto());
		return "master/transferStock"; //html
	}
	
	@PostMapping("/updateStockTransfer")
	public String updateStockTransfer(@Valid @ModelAttribute("stockTranDto") StockTransferDto stockTranDto, 
			BindingResult bindingResult, RedirectAttributes ra, Model model) throws SQLException {
		
		  boolean stockFlg=false;
		  
		  try {
			stockFlg = masterService.transferStock(stockTranDto.getFromStore(), stockTranDto.getToStore(),
					stockTranDto.getMaterialCode().split("#")[0], stockTranDto.getQuantity());
		} catch (Exception e) {
			
		}
		if(stockFlg) { 
		  ra.addFlashAttribute("success","Stock Details Updated successfully.");
		  
		  
		  } else { 
			  ra.addFlashAttribute("error","Error while updating Stock."); 
		} 
		
		return "redirect:/master/showTransferStock"; //html
	}
	
	@GetMapping("/showReduceStock")
	public String reduceStock(Model model) { 
		
		List<EzStores> storeList=masterService.listStores();
		model.addAttribute("storeList",storeList);
		model.addAttribute("materialDto", new MaterialDto());
		return "master/reduceStock"; //html
	}
	
	@PostMapping("/updateStockReduce")
	public String updateStockReduce(@Valid @ModelAttribute("materialDto") MaterialDto materialDto, 
			BindingResult bindingResult, RedirectAttributes ra, Model model) throws SQLException {
		
		  boolean stockFlg=false;
		  
		  try {
			stockFlg = masterService.reduceStock(materialDto.getStockLoc(),materialDto.getMaterialCode().split("#")[0], materialDto.getQuantity());
		} catch (Exception e) {
			
		}
		if(stockFlg) { 
		  ra.addFlashAttribute("success","Stock Details reduced successfully.");
		  
		  
		  } else { 
			  ra.addFlashAttribute("error","Error while reducing Stock."); 
		} 
		
		return "redirect:/master/showReduceStock"; //html
	}
	 @RequestMapping(value = "/showPlumbers", method = RequestMethod.GET)
	    public String showPlumbers(Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {
	    	
		 	model.addAttribute("plumberList", masterService.getPlumberList());
	        return "master/plumberMaster"; 

	    }
	 @GetMapping("/showUserGrpList")
		public String userGroupList(@ModelAttribute("userGrpDto") UserGroupDto userGrpDto,Model model) {
			
		 	List<String> grpTypeList=new ArrayList<String>();
		 	grpTypeList.add("States");
		 	grpTypeList.add("Zones");
		 	if(userGrpDto == null || userGrpDto.getGrpType() == null)
		 	{
		 		userGrpDto=new UserGroupDto();
		 		userGrpDto.setGrpType("States");
		 	}
		 	String roleStr="";
		 	if("States".equals(userGrpDto.getGrpType()))
		 		roleStr="ROLE_REQ_CR";
		 	else if("Zones".equals(userGrpDto.getGrpType()))
		 		roleStr="ROLE_ZN_HEAD";
		 	
			List<Work_Groups> groupList=iUserService.getGroupsByRole(roleStr);
			userGrpDto.setGroupList(groupList);
			model.addAttribute("grpTypeList",grpTypeList);
			model.addAttribute("userGrpDto",userGrpDto);
			return "master/listWorkGroup"; //html
		}
	 @GetMapping("/showUserGrpAdd")
		public String userGroupAdd(Model model) {
		 
		    UserGroupAddDto userGrpAddDto= new UserGroupAddDto();
		 
		 	List<String> grpTypeList=new ArrayList<String>();
		 	grpTypeList.add("States");
		 	grpTypeList.add("Zones");
		 	
			model.addAttribute("grpTypeList",grpTypeList);
			model.addAttribute("userGrpAddDto",userGrpAddDto);
			return "master/addWorkGroup"; //html
		}
	 
	 @PostMapping("/saveuserGroup")
		public String saveuserGroup(@Valid @ModelAttribute("userGrpAddDto") UserGroupAddDto userGrpAddDto, 
				BindingResult bindingResult, RedirectAttributes ra, Model model) throws SQLException {
			
			
				String group=userGrpAddDto.getGroupId();
				String groupType=userGrpAddDto.getGroupType();
				String roleStr="";
			 	if("States".equals(groupType))
			 		roleStr="ROLE_REQ_CR";
			 	else if("Zones".equals(groupType))
			 		roleStr="ROLE_ZN_HEAD";
				log.debug("groupType"+groupType);
				
				Work_Groups workGrpObj = iUserService.findGroupByRoleAndGroup(roleStr, group);
				if(workGrpObj == null)
				{
					workGrpObj = new Work_Groups(group,userGrpAddDto.getDescription(),roleStr);
					iUserService.saveWorkGroup(workGrpObj);
					ra.addFlashAttribute("success", "Workgroups Saved successfully.");
					
					
				}
				else
				{	
					ra.addFlashAttribute("error", "Workgroup "+workGrpObj.getName()+" already exists");
				}
			
			return "redirect:/master/showUserGrpAdd"; //html
		}

}

