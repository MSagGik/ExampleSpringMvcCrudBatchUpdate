package ru.msaggik.spring.contriller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.msaggik.spring.dao.PeopleDAO;

@Controller
@RequestMapping("/test-batch-update")
public class BatchController {

    private final PeopleDAO personDAO;

    @Autowired
    public BatchController(PeopleDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping
    public String index() {
        return "batch/index";
    }

    @GetMapping("/without") // без пакетов
    public String withoutBatch() {
        personDAO.testMultipleUpdate();
        return "redirect:/people";
    }

    @GetMapping("/with") // с пакетной вставкой
    public String withBatch() {
        personDAO.testBatchUpdate();
        return "redirect:/people";
    }

}
