package cz.metacentrum.perun.core.implApi.modules.attributes;

import cz.metacentrum.perun.core.api.Attribute;
import cz.metacentrum.perun.core.api.AttributeDefinition;
import cz.metacentrum.perun.core.api.Vo;
import cz.metacentrum.perun.core.api.exceptions.InternalErrorException;
import cz.metacentrum.perun.core.api.exceptions.WrongAttributeAssignmentException;
import cz.metacentrum.perun.core.api.exceptions.WrongAttributeValueException;
import cz.metacentrum.perun.core.api.exceptions.WrongReferenceAttributeValueException;
import cz.metacentrum.perun.core.impl.PerunSessionImpl;

/**
 * This interface serves as a template for checking and filling in Virtual
 * Organization (Vo) attributes.
 *
 * @author Lukáš Pravda <luky.pravda@gmail.com>
 */
public interface VoAttributesModuleImplApi extends AttributesModuleImplApi {

    /**
     * This method tries to fill appropriate Virtual Organization (Vo) attributes.
     *
     * @param perunSession Perun session
     * @param vo Virtual Organization
     * @param attribute Attribute of which you'd like to check.
     * @return attribute which MAY by filled in.
     *
     * @throws InternalErrorException if an exception is raised in particular
     *         implementation, the exception is wrapped in InternalErrorException
     * @throws WrongAttributeAssignmentException
     */
    Attribute fillAttribute(PerunSessionImpl perunSession, Vo vo, AttributeDefinition attribute) throws InternalErrorException, WrongAttributeAssignmentException;

    /**
     * Method for checking Vo attributes.
     *
     * @param perunSession Perun session
     * @param vo Virtual Organization
     * @param attribute attribute to be checked
     *
     * @throws InternalErrorException if an exception is raised in particular
     *         implementation, the exception is wrapped in InternalErrorException
     * @throws WrongAttributeValueException if the attribute value is wrong/illegal
     * @throws WrongReferenceAttributeValueException if an referenced attribute against
     *         the parameter is to be compared is not available.
     * @throws WrongAttributeAssignmentException
     */
    void checkAttributeValue(PerunSessionImpl perunSession, Vo vo, Attribute attribute) throws InternalErrorException, WrongAttributeValueException, WrongReferenceAttributeValueException, WrongReferenceAttributeValueException, WrongAttributeAssignmentException;

    /**
     * If you need to do some further work with other modules, this method do that
     *
     * @param session session
     * @param vo the vo
     * @param attribute the attribute
     */
    void changedAttributeHook(PerunSessionImpl session, Vo vo, Attribute attribute) throws InternalErrorException, WrongAttributeValueException, WrongReferenceAttributeValueException, WrongAttributeAssignmentException;
}
