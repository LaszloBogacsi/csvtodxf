package com.csvtodxf;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ConvertService extends Service<String> {
    @Override
    protected Task<String> createTask() {
        return new Task<String>() {

            @Override
            protected String call() throws Exception {
                updateMessage("message");

                return null;
            }
        };
    }
}
