package uk.ac.liv.jt.format.elements;

import uk.ac.liv.jt.types.CoordF32;

public class ModelView {

    CoordF32 eye_direction;
    float angle;
    CoordF32 eye_pos;
    CoordF32 target_point;
    CoordF32 view_angle;
    float viewport_diameter;
    float reserved1;
    int reserved2;
    int active_flag;
    int view_id;
    int view_name_string_id;
    
}
