package st.evening.mc.groovyworld.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public class ScanBlockPos extends BlockPos.MutableBlockPos {
    public ScanBlockPos(final int x, final int y, final int z) {
        super(x, y, z);
    }

    public ScanBlockPos(final double x, final double y, final double z) {
        super(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

    public ScanBlockPos(final BlockPos pos) {
        super(pos);
    }

    public void setX(final int x) {
        this.x = x;
    }

    public void setZ(final int z) {
        this.z = z;
    }

    // slightly awkward name but distinguishes it from the non-mutating add(...) functions
    public void setAddOffset(final int dx, final int dy, final int dz) {
        setPos(x + dx, y + dy, z + dz);
    }

    public void setAddOffset(final Vec3i offset) {
        setAddOffset(offset.getX(), offset.getY(), offset.getZ());
    }

    public void setAddOffset(final EnumFacing dir, final int offset) {
        switch (dir) {
            case UP:
                setY(y + offset);
                break;
            case DOWN:
                setY(y - offset);
                break;
            case NORTH:
                setZ(z - offset);
                break;
            case EAST:
                setX(x + offset);
                break;
            case SOUTH:
                setZ(z + offset);
                break;
            case WEST:
                setX(x - offset);
                break;
        }
    }
}
