package com.ezc.hsil.webapp.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezc.hsil.webapp.dto.DistributorDto;
import com.ezc.hsil.webapp.dto.MaterialDto;
import com.ezc.hsil.webapp.model.DistributorMaster;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.service.IMasterService;
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
	@PostMapping("/delete-dist/{id}")
	public String deleteDistributor(@PathVariable("code") String code) {
		
			masterService.deleteDistributor(code);
			return "redirect:/master/listDis";
	}
	
	@GetMapping("/addMaterial")
	public String showMaterialForm(Model model) {
		
		model.addAttribute("materialDto", new MaterialDto());
		return "master/addMaterial"; //html
	}
	@PostMapping("/addMaterial")
	public String saveMaterail(@Valid @ModelAttribute("materialDto") MaterialDto mDto, 
			BindingResult bindingResult, RedirectAttributes ra, Model model) {
		
		if(bindingResult.hasErrors()) {
			
			log.info("bindingResult:::::of material {}",bindingResult);
			return "master/addMaterial";
		}else {
			
			ra.addFlashAttribute("success", "Material added successfully.");
			masterService.addNewMaterial(mDto);
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
	
	@RequestMapping(value = "/distSamp", method = RequestMethod.GET)
    public StreamingResponseBody getDistSample(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel"); 
        response.setHeader("Content-Disposition", "attachment; filename=\"distributors.xls\"");
        String [] distCodeArr={"Distributor Code","Distributor Name","Contact No","Business Unit Name","City"};
        return ezExcelUtil.writeExcel(distCodeArr,null,"distributors"); 
    }
	
	@RequestMapping(value = "/matSamp", method = RequestMethod.GET)
    public StreamingResponseBody getMastSample(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel"); 
        response.setHeader("Content-Disposition", "attachment; filename=\"material.xls\"");
        String [] matArr={"Material Code","Material Desc","Quantity"};
        return ezExcelUtil.writeExcel(matArr,null,"material"); 
    }
	@RequestMapping(value = "/distDown", method = RequestMethod.GET)
    public StreamingResponseBody getDistDownload(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel"); 
        response.setHeader("Content-Disposition", "attachment; filename=\"distributors.xls\"");
        String [] distCodeArr={"Distributor Code","Distributor Name","Contact No","Business Unit Name","City"};

        List<DistributorMaster> distMastList= masterService.findAll();
        List<Object[]> objArrList=null;
		try {
			objArrList = getObjectArray(distMastList,5);
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

        response.setContentType("application/vnd.ms-excel"); 
        response.setHeader("Content-Disposition", "attachment; filename=\"material.xls\"");
        String [] matCodeArr={"Material Code","Material Name","Quantity"};

        List<MaterialMaster> matMastList= masterService.findMatAll();
        List<Object[]> objArrList=null;
		try {
			objArrList = getObjectArray(matMastList,3);
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
        return ezExcelUtil.writeExcel(matCodeArr,objArrList,"materials"); 
    }

	@PostMapping("/distUpload")
	public void uploadDistributor(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {
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
				 distList.add(distMaster);
		 }
		 masterService.addDistributorMultiple(distList);
	    }
	@PostMapping("/matUpload")
	public void uploadMaterial(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {
		List<MaterialMaster> matList = new ArrayList<MaterialMaster>();
		List<Object[]> lisObjArr= ezExcelUtil.readExcel(reapExcelDataFile.getInputStream(),reapExcelDataFile.getOriginalFilename());
		 for(Object[] objArr:lisObjArr)
		 {
			 	 MaterialMaster matMaster = new MaterialMaster();
			 	 String quan=(String)objArr[2];
			 	int qty=0;
			 	try {
			 		qty = Integer.parseInt(quan);

			 	} catch(NumberFormatException e) {
			 	    double d = Double.parseDouble(quan); 
			 	   qty = (int) d;
			 	}
			 	 matMaster.setMaterialCode((String)objArr[0]);
			 	 matMaster.setMaterialDesc((String)objArr[1]);
			 	 matMaster.setQuantity(qty);
			 	 matList.add(matMaster);
		 }
		 masterService.addMaterialMultiple(matList);
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
}
