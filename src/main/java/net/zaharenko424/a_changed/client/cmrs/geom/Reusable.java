package net.zaharenko424.a_changed.client.cmrs.geom;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Reusable {

    public static final ThreadLocal<Vector3f> VEC3F = ThreadLocal.withInitial(Vector3f::new);
    public static final ThreadLocal<Quaternionf> QUATERNION = ThreadLocal.withInitial(Quaternionf::new);
}