package io.openliberty.guides.todo.services.samples;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import io.openliberty.guides.todo.models.TodoModel;
import io.openliberty.guides.todo.services.TodoService;

@Alternative
@ApplicationScoped
public class SampleTodoService implements TodoService {
    private List<TodoModel> todos;
    private static int id = 0;
}
