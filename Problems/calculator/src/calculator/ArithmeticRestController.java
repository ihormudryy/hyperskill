package calculator;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class ArithmeticRestController {

    public String calculate(String operation, int a, int b) {
        switch (operation) {
            case "add": {
                return String.valueOf(a + b);
            }
            case "subtract": {
                return String.valueOf(a - b);
            }
            case "mult": {
                return String.valueOf(a * b);
            }
        }
        return "Unknown operation";
    }

    @RequestMapping(value = "/add", method = GET)
    @ResponseBody
    public ResponseEntity add(@RequestParam("a") int a, @RequestParam("b") int b) {
        String res = calculate("add", a, b);
        return new ResponseEntity<String>(res, HttpStatus.OK);
    }

    @GetMapping(value="/mult")
    public ResponseEntity mult(@RequestParam("a") int a, @RequestParam("b") int b) {
        String res = calculate("mult", a, b);
        return new ResponseEntity<String>(res, HttpStatus.OK);
    }

    @GetMapping(value="/subtract")
    public ResponseEntity subtract(@RequestParam("a") int a, @RequestParam("b") int b) {
        String res = calculate("subtract", a, b);
        return new ResponseEntity<String>(res, HttpStatus.OK);
    }

    @GetMapping(value="/divide")
    public ResponseEntity divide(@RequestParam("a") int a, @RequestParam("b") int b) {
        String res = calculate("divide", 1, 1);
        System.out.println("divide");
        return new ResponseEntity<String>(res, HttpStatus.OK);
    }

    @GetMapping(value="/*")
    public ResponseEntity all(@RequestParam("a") int a, @RequestParam("b") int b) {
        return new ResponseEntity<String>("Unknown operation", HttpStatus.OK);
    }
}