/**
 * 
 */
package com.agencybanking.security.tokens;

import com.agencybanking.core.web.Paging;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author dubic 
 *
 */
@RestController
@RequestMapping("/token")
public class TokenController {

	private TokenService tokenService;
    private TokenRepository tokenRepository;

    public TokenController(TokenService service, TokenRepository tokenRepository){
        this.tokenService = tokenService;
        this.tokenRepository = tokenRepository;
    }

	@PutMapping("/create")
	public ResponseEntity<?> createToken(@RequestBody Token token) {
		return ResponseEntity.ok(tokenService.create(token));
	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<?> findById(@PathVariable("id") Token token) {
		return ResponseEntity.ok(token);
	}

	@PostMapping("/findAll")
	public ResponseEntity<?> findAll(@RequestBody Paging query) {
		return ResponseEntity.ok(tokenService.query(null, PageRequest.of(query.getPage(), query.getLimit(), query.getSort())));
	}

	@PatchMapping("/update")
	public ResponseEntity<?> update(@RequestBody Token token) {
		return ResponseEntity.ok(tokenService.update(token));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Token token) {
        tokenService.delete(token);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PostMapping("/metrics/count")
	public ResponseEntity<?> metricsMount(@RequestBody Token token) {
		return ResponseEntity.ok(this.tokenRepository.count(Example.of(token)));
	}
}