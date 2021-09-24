package presenter;

import java.util.List;

public interface BasicPresenter {
    String update(String prompt, String input);
    String checkInputs(String title, List<String> prompts, List<String> inputs);
}
