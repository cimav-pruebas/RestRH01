/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.awt.Color;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author juan.calderon
 */
@Converter
public class ColorConverter implements AttributeConverter<Color, String> {

    @Override
    public String convertToDatabaseColumn(Color color) {
        StringBuilder sb = new StringBuilder();
        sb.append(Color.RED.equals(color) ? "RED" : "GREEN");
        return sb.toString();
    }

    @Override
    public Color convertToEntityAttribute(String colorString) {
        if (colorString.contains("RED")) {
            return Color.RED;
        } else {
            return Color.GREEN;
        }
    }

}
