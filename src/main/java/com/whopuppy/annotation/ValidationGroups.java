package com.whopuppy.annotation;

import javax.validation.groups.Default;

/**
 * Utility classes to distinct CRUD validations.<br>
 * <br>
 * Used with the
 * {@link org.springframework.validation.annotation.Validated @Validated}
 * Spring annotation.
 */
public final class ValidationGroups {

    private ValidationGroups() { }

    public interface signUp extends  Default{};
    public interface logIn extends Default{};
    public interface sendMessage extends Default{};
    public interface configMessage extends Default{};
}