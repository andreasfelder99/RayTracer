package ch.andreasfelder.rayTracer;

import ch.andreasfelder.rayTracer.brdf.BrdfNormal;
import ch.andreasfelder.rayTracer.brdf.BrdfReflective;
import ch.andreasfelder.rayTracer.brdf.IBrdf;
import ch.andreasfelder.rayTracer.texture.BitMapTexture;
import ch.andreasfelder.rayTracer.texture.UniColorTexture;
import ch.andreasfelder.vector.Vector3;

public class Scenes {
    private static IBrdf brdfNormal = new BrdfNormal();
    private static IBrdf brdfReflect = new BrdfReflective(SphereColor.WHITE, 3f);

    public static Scene getCornellScene() {
        return new Scene(new Sphere[] {
            new Sphere(new Vector3(-1001, 0, 0), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.RED)),
            new Sphere(new Vector3(1001, 0, 0), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.BLUE)),
            new Sphere(new Vector3(0, 0, 1001), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.GRAY)),
            new Sphere(new Vector3(0, -1001, 0), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.GRAY)),
            new Sphere(new Vector3(0, 1001, 0), 1000, SphereColor.WHITE, brdfNormal,
                new UniColorTexture(SphereColor.WHITE)),
            new Sphere(new Vector3(-0.6, -0.7, -0.6), 0.3F, SphereColor.BLACK, brdfReflect,
                new UniColorTexture(SphereColor.YELLOW)),
            new Sphere(new Vector3(0.3, -0.4, 0.3), 0.6F, SphereColor.BLACK, brdfReflect,
                new UniColorTexture(SphereColor.CYAN))
        });
    }

    public static Scene getCornellWithTextures() {
        return new Scene(new Sphere[] {
            new Sphere(new Vector3(-1001, 0, 0), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.RED)),
            new Sphere(new Vector3(1001, 0, 0), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.BLUE)),
            new Sphere(new Vector3(0, 0, 1001), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.GRAY)),
            new Sphere(new Vector3(0, -1001, 0), 1000, SphereColor.BLACK, brdfNormal,
                new BitMapTexture("/woodFloor.jpg")),
            new Sphere(new Vector3(0, 1001, 0), 1000, SphereColor.WHITE, brdfNormal,
                new UniColorTexture(SphereColor.WHITE)),
            new Sphere(new Vector3(-0.6, -0.7, -0.6), 0.3F, SphereColor.BLACK, brdfReflect,
                new BitMapTexture("/marsSurface.jpg")),
            new Sphere(new Vector3(0.3, -0.4, 0.3), 0.6F, SphereColor.BLACK, brdfReflect,
                new BitMapTexture("/bricks2.jpg"))
        });
    }

    public static Scene getGalaxyScene() {
        return new Scene(new Sphere[] {
            // Walls
            new Sphere(new Vector3(-1001, 0, 0), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.GRAY)), // Left wall
            new Sphere(new Vector3(1001, 0, 0), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.GRAY)), // Right wall
            new Sphere(new Vector3(0, 0, 1001), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.GRAY)), // Back wall
            new Sphere(new Vector3(0, -1001, 0), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.GRAY)), // Floor
            new Sphere(new Vector3(0, 1001, 0), 1000, SphereColor.WHITE, brdfNormal,
                new UniColorTexture(SphereColor.GRAY)), // Ceiling

            // Galaxy-like small spheres
            new Sphere(new Vector3(-0.5, -0.5, -0.5), 0.1F, SphereColor.BLACK, brdfReflect,
                new BitMapTexture("/venusSurface.jpg")), // Star
            new Sphere(new Vector3(0.5, -0.4, 0.6), 0.15F, SphereColor.BLACK, brdfReflect,
                new UniColorTexture(SphereColor.BLUE)), // Star
            new Sphere(new Vector3(-0.3, -0.3, 0.3), 0.2F, SphereColor.BLACK, brdfNormal,
                new BitMapTexture("/sun.jpg")), // Star
            new Sphere(new Vector3(0.2, -0.2, -0.3), 0.25F, SphereColor.BLACK, brdfReflect,
                new BitMapTexture("/marsSurface.jpg")), // Star
            new Sphere(new Vector3(-0.1, -0.1, 0.1), 0.05F, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.MAGENTA)) // Small central star
        });
    }
    public static Scene getAntiAliasingScene() {
        return new Scene(new Sphere[] {
            // Walls
            new Sphere(new Vector3(-1001, 0, 0), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.RED)), // Left wall
            new Sphere(new Vector3(1001, 0, 0), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.BLUE)), // Right wall
            new Sphere(new Vector3(0, 0, 1001), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.GRAY)), // Back wall
            new Sphere(new Vector3(0, -1001, 0), 1000, SphereColor.BLACK, brdfNormal,
                new UniColorTexture(SphereColor.GRAY)), // Floor
            new Sphere(new Vector3(0, 1001, 0), 1000, SphereColor.WHITE, brdfNormal,
                new UniColorTexture(SphereColor.WHITE)), // Ceiling

            new Sphere(new Vector3(0, -0.35, -0.5), 0.25F, SphereColor.BLACK, brdfReflect,
                new BitMapTexture("/fineGrid.jpg"))
        });
    }
}
