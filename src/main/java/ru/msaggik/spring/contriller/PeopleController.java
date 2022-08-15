package ru.msaggik.spring.contriller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.msaggik.spring.dao.PeopleDAO;
import ru.msaggik.spring.models.Person;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {
    // внедрение бина PeopleDAO в данный контроллер
    private final PeopleDAO peopleDAO;
    @Autowired
    public PeopleController(PeopleDAO peopleDAO) {
        this.peopleDAO = peopleDAO;
    }
    // получение списка всех пользователей из DAO и передача его в представление
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleDAO.index());
        return "people/index";
    }
    // получение информации одного пользователя по id из DAO и передача её в представление
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        // "@PathVariable("id") int id" - извлечение данных пользователя с id
        model.addAttribute("person", peopleDAO.show(id));
        return "people/show";
    }
    // возврат html формы для создания информации о новом пользователе
    @GetMapping("/new")
    public String newPerson(Model model) {
        model.addAttribute("person", new Person());
        return "people/new";
    }
    // принимает POST запрос и отправляет данные в БД для создания информации о пользователе
    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {  // @Valid - задание валидности данных пользователя
        // возможная ошибка помещается в объект BindingResult
        if (bindingResult.hasErrors()) // в случае ошибки происходит выброс на "people/new" с указанием произошедших ошибок
            return "people/new";
        peopleDAO.save(person);
        return "redirect:/people"; // redirect - переброска на другой домен
    }
    // редактирование данных пользователя:
    // 1) отображение данных имеющегося пользователя
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleDAO.show(id));
        return "people/edit";
    }
    // 2) приём PATH запроса
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";
        peopleDAO.update(id, person);
        return "redirect:/people";
    }
    // удаление данных пользователя
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleDAO.delete(id);
        return "redirect:/people";
    }
}
