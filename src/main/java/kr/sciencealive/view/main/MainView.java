package kr.sciencealive.view.main;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

/**
 * Back-office shell for the admin role (entity inspector, role management).
 * The public-facing surfaces are separate Vaadin routes hosted in
 * {@code PublicLayout}; this standard Jmix main view is the post-login
 * landing place for administrators.
 */
@Route("admin")
@ViewController(id = "MainView")
@ViewDescriptor(path = "main-view.xml")
public class MainView extends StandardMainView {
}
