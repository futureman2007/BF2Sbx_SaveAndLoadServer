package sbx.revive;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/test")
	public String index() {
		System.out.println("calles /test :D");
		return "This is the BF2 Sandbox server for @netsavegroup <groupname> and @netloadgroup <groupname>";
	}

}