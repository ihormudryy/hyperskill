package platform.controllers;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import platform.db.CodeRepository;
import platform.model.Code;

@Controller
public class CodeController {

    @Autowired
    public CodeRepository codeRepository;

    private void validateCodeEntry(Code entry) {
        if (entry.isRestrictedCompletely()) {
            if (!entry.isStillValid()) {
                codeRepository.delete(entry);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, entry.getId() + " not found");
            }
            entry.setOneVisit();
            entry.subtractTimeDelta();
            codeRepository.save(entry);
        }
    }

    private Stream getEntries() {
        return codeRepository.findAll()
                             .stream()
                             .filter(e -> !e.isRestrictedCompletely())
                             .sorted((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()))
                             .limit(10);
    }

    @GetMapping(value = "/code/{id}")
    public String getCodePage(@PathVariable String id, Model model) {
        switch (id) {
            case "latest":
                model.addAttribute("codes", getEntries()
                    .collect(Collectors.toList()));
                model.addAttribute("title", "Latest");
                return "code";
            case "new":
                return "new";
            default:
                try (Code entry = codeRepository.getOne(id)) {
                    validateCodeEntry(entry);
                    model.addAttribute("timeRestriction", entry.getTimeOut() != 0 ? true : false);
                    model.addAttribute("viewRestriction", entry.isViewRestricted() ? true : false);
                    model.addAttribute("code", entry);
                    model.addAttribute("title", "Code");
                    return "index";
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + " not found");
                }
        }
    }

    @GetMapping(value = "/api/code/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCodeById(@PathVariable String id) throws IOException {
        switch (id) {
            case "latest":
                return ResponseEntity.ok(getEntries().map(c -> c.toString())
                                                     .collect(Collectors.joining(",\n", "[", "]")));
            default:
                try (Code entry = codeRepository.getOne(id)) {
                    validateCodeEntry(entry);
                    return ResponseEntity.ok(entry.toString());
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + " not found");
                }
        }
    }

    @PostMapping(value = "/api/code/new")
    public ResponseEntity receiveNewCode(@RequestBody Code code) throws IOException {
        codeRepository.save(code);
        return ResponseEntity.ok(String.format("{ \"id\" : \"%s\" }", code.getId()));
    }
}
