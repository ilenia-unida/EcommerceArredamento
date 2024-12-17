package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;

import jakarta.mail.MessagingException;

import java.util.ArrayList;

@Controller
public class MyController {
	
	@Autowired
	JdbcTemp p1;
	
	@Autowired
	EmailService emailService;
	
	@Value("${stripe.currency}")
	private String currency;
	
	@Value("${stripe.secretKey}")
    private String stripeSecretKey;
	/* rotte lato gestore */
	
	@GetMapping("private/form")
	public String getForm(Model model) {
		
		ArrayList <arredo> lista = p1.getLista();
		
		model.addAttribute("lista", lista);
		
		return "form";
	}
	
	@ResponseBody
	@PostMapping("/submit")
	public String gestisciForm(@RequestParam("nome") String nome, 
			@RequestParam("marca") String marca,
			@RequestParam("prezzo") double prezzo,
			@RequestParam("url") String url
			) {
		
		p1.insertMobile(nome, marca, prezzo, url);
				return nome + " aggiunto con successo";
		
		
		
		
	}
	
	@ResponseBody
	@PostMapping("/update")
	public String updatePrezzo(@RequestParam("nome") String nome,
			@RequestParam("prezzo") double prezzo
			) {
		p1.setPrezzo(nome, prezzo);
		return nome + " aggiornato con successo";
		
	}
	@ResponseBody
	@PostMapping("/delete")
	public String delete(@RequestParam("nome") String nome) {
		
		p1.delete(nome);
		
		
		return nome + "Cancellato con successo";
	}
	
	
	
	
	@GetMapping("/store")
	public String getStore(Model model) {
ArrayList <arredo> lista = p1.getLista();
		
		model.addAttribute("lista", lista);
		return "store";
	}
	
	@GetMapping("/")
	public String getHome(Model model) {
ArrayList <arredo> lista = p1.getLista();
		
		model.addAttribute("lista", lista);
		return "home";
	}
	
	
	
	@GetMapping("/carrello")
	public String getCarrello(Model model) {
ArrayList <arredo> lista = p1.getLista();
		
		model.addAttribute("lista", lista);
		return "carrello";
	}
	
	
	@GetMapping("/private/inventory")
	public String getInventory(Model model) {
	    ArrayList<arredo> inventoryList = p1.getLista(); // Obtiene la lista de productos
	    model.addAttribute("inventoryList", inventoryList); // Pasa la lista al modelo
	    return "inventory"; // Nombre de la vista
	}
	@PostMapping("/buy")
	public String recap(
			@RequestParam("mail") String mail, @RequestParam("token") String token, 
			
			Model model) throws MessagingException {
		
		int somma = 0;
		ArrayList <arredo> lista = p1.getLista();
		ArrayList <arredo2> listaP = new ArrayList<>();
		ArrayList <String> listaU = new ArrayList <>();
				
	    String soggetto = "Hai acquistato: ";
				
				for (int i = 0; i < carrello.size(); i++) {
			
			
			
			if (carrello.get(i).pezzi > 0 ) {
			//System.out.println("Hai acquistato " + listaP.get(i).getNome() + " marca " + listaP.get(i).getMarca()  );
			//System.out.println("In " + selected.get(i) + " pezzi");
			somma += carrello.get(i).pezzi * carrello.get(i).prezzo;
			
			p1.change(carrello.get(i).nome, carrello.get(i).pezzi);
			listaU.add(carrello.get(i).url);
			soggetto +=   carrello.get(i).nome + ", ";
			
		}
		}
	    soggetto += " La somma totale da pagare è: " + somma + " euro";
		emailService.sendEmailWithImage(mail, "mail da talentform-giocattoli", soggetto, listaU);
		
		//System.out.println("La somma totale da pagare è: " + somma + " euro");
		model.addAttribute("lista", carrello);
		model.addAttribute("somma", somma);
		
		  try {
	            // Imposta la chiave segreta di Stripe
	            Stripe.apiKey = stripeSecretKey;

	            // Crea una richiesta di pagamento
	            ChargeCreateParams params = ChargeCreateParams.builder()
	                    .setAmount((long)  somma * 100) // Importo in centesimi
	                    .setCurrency("eur")
	                    .setSource(token) // Usa il token di test fornito
	                    .build();
	            
	            
	            Charge charge = Charge.create(params);
	            
	            System.out.println("Pagamento completato: " + charge.toJson());
	            
	            System.out.println("Visualizza ricevuta: " +  charge.getReceiptUrl());
	            
	         //risult = "Pagamento andato a buon fine";
	         
	         model.addAttribute("urlRicevuta",charge.getReceiptUrl());
         //  ok = true;
	            
	            
	        } catch (StripeException e) {
	        	
	        	 
	        }
		
		
		
		
		return "recap";
	}
	@CrossOrigin(origins = "*") 
	@ResponseBody
	@GetMapping("/lista")
	public ArrayList<arredo> getLista(){
		
		ArrayList<arredo> lista = p1.getLista();
		
		return lista;
		
	}
	ArrayList<arredo2> carrello = new ArrayList<>();
	@PostMapping("/add")
	public String add(Model model, @RequestParam("selected")  int selectedG,@RequestParam("nome") String nome  ) {
		
		
		
		ArrayList <arredo> listaG = p1.getLista();
		System.out.println(selectedG);
		System.out.println(nome);
		boolean trovato = false;
		
		int indice = -1;
		
		
		
    	  
    	  if (carrello.size() == 0) {
    		  for (int i = 0; i < listaG.size(); i++ ) {
    			  
    			  
    			  if (listaG.get(i).nome.equals(nome)) {
				
				arredo2 g1 = new arredo2();
				g1.nome = listaG.get(i).nome;
				g1.marca = listaG.get(i).marca;
				g1.url = listaG.get(i).url;
				g1.prezzo = listaG.get(i).prezzo;
				g1.pezzi = selectedG; 
				System.out.println("sono il primo");
				carrello.add(g1);
				
			}}}
    	  
    	  else {
    		 System.out.println("sono qui");

    			 for (int i = 0; i < listaG.size(); i++ ) {
			
		
		if (listaG.get(i).nome.equals(nome)) {
			
			for (int j = 0; j < carrello.size(); j++) {
				
				
				System.out.println(carrello.get(j).nome);
				
				if (carrello.get(j).nome.equals(nome) == true) {
			System.out.println(trovato);
			trovato = true;
			
			/**/
				}
				else {
					indice = i;
				}
		}
		
		}
	}
    	 
		 
		 
		 
		 
		 
if (trovato == false) {
    		 
    		 arredo2 g1 = new arredo2();
 			g1.nome = listaG.get(indice).nome;
 			g1.marca = listaG.get(indice).marca;
 			g1.url = listaG.get(indice).url;
 			g1.prezzo = listaG.get(indice).prezzo;
 			g1.pezzi = selectedG; 
 			System.out.println("inserito perchè diverso");
 			carrello.add(g1);
    	 }
else {
	
	for (int j = 0; j < carrello.size(); j++) {
		
		
		System.out.println(carrello.get(j).nome);
		
		if (carrello.get(j).nome.equals(nome) == true) {
	
	arredo2 g1 = new arredo2();
		carrello.get(j).pezzi = selectedG;

	
}}}}
		
		model.addAttribute("carrello", carrello);
		
		return getStore(model);
	}
	
	
	
	@PostMapping("/del")
	public String del(Model model, @RequestParam("nome") String nome) {
	   
	    carrello.removeIf(item -> item.nome.equals(nome));

	    
	    model.addAttribute("carrello", carrello);

	    
	    return getStore(model);
	}
	
	@PostMapping("/clear")
	public String clear(Model model) {
		
		carrello.clear();
		
model.addAttribute("carrello", carrello);
		
		return getStore(model);
		
	}
	

}

