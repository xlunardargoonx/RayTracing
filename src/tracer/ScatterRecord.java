package tracer;

import tracer.Ray;
import tracer.Vector3;
import tracer.pdf.PDF;

public class ScatterRecord {
    Ray specular_ray;
    boolean is_specular;
    Vector3 attenuation;
    PDF pdf_ptr;

    public ScatterRecord() {
        specular_ray = new Ray();
        attenuation = new Vector3();
        is_specular = false;
    }

    public Ray getSpecular_ray() {
        return specular_ray;
    }

    public void setSpecular_ray(Ray specular_ray) {
        this.specular_ray = specular_ray;
    }

    public boolean isSpecular() {
        return is_specular;
    }

    public void setIsSpecular(boolean is_specular) {
        this.is_specular = is_specular;
    }

    public Vector3 getAttenuation() {
        return attenuation;
    }

    public void setAttenuation(Vector3 attenuation) {
        this.attenuation = attenuation;
    }

    public PDF getPdf_ptr() {
        return pdf_ptr;
    }

    public void setPdf_ptr(PDF pdf_ptr) {
        this.pdf_ptr = pdf_ptr;
    }
}
