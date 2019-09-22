package com.cg.certService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.certService.Journal;
import com.cg.certService.domain.Budget;
import com.cg.certService.domain.BudgetDb;
import com.cg.certService.domain.Cert;
import com.cg.certService.domain.Composite;
import com.cg.certService.domain.MasterDb;
import com.cg.certService.domain.MasterProvision;
import com.cg.certService.message.Message;
import com.cg.certService.message.MessageSender;

import io.opentracing.Span;
import io.opentracing.Tracer;


@RequestMapping("adif")
@RestController
public class CertController {
	
	@Autowired
	  private MessageSender messageSender;
	
	@Autowired
	private MasterDb db;
	
	@Autowired
	private BudgetDb db1;
	
	
	 @Autowired
	    private Tracer tracer;
	 
	 Journal journal = new Journal();
	 
	 private static final org.slf4j.Logger Logger =
	            org.slf4j.LoggerFactory.getLogger(CertController.class);
	
	@CrossOrigin
	@PostMapping("/cert")
	public void initialProcess(@RequestBody Cert e) {
		Span span = tracer.buildSpan("Initiating Status Request").start();
		Message<Cert> message = new Message<Cert>("enCursoEvent", e);
		span.finish();
		messageSender.send(message);	
		
	}
	
	//to add values in master schema
	@CrossOrigin
	@PostMapping("/master")
	public MasterProvision CreateNew(@RequestBody Composite c)
	{
		MasterProvision pro = new MasterProvision();
		pro.setKey(c);
		
		return db.saveAndFlush(pro);
	}
	
	@CrossOrigin
	@GetMapping("/master/list")
	public List<MasterProvision> getAll()
	{
		journal.log("Succesfully listed the master db entries");
		return db.findAll();
		
	}
	
	@CrossOrigin
	@GetMapping("/budget/list")
	public List<Budget> getAllCodigo()
	{
		return db1.findAll();
	}
	
	

}
