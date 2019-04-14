package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="localDateTimeConverter")
public class LocalDateTimeConverter implements javax.faces.convert.Converter {
 
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
          return LocalDate.parse(value, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
 
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
 
        LocalDate dateValue = (LocalDate) value;
         
        return dateValue.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
     
}