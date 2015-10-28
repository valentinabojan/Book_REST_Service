package business_layer.value_objects;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate>{

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        if (v != null)
            return LocalDate.parse(v);

        return null;
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        if (v != null)
            return v.toString();

        return null;
    }
}
