package mrriegel.decoy;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Transfer {
	public BlockPos dis;
	public Pair<BlockPos, EnumFacing> rec;
	public Vec3d current;
	public ItemStack stack;

	private Transfer() {
	}

	public void readFromNBT(NBTTagCompound compound) {
		NBTTagCompound c = compound.getCompoundTag("stack");
		stack = ItemStack.loadItemStackFromNBT(c);
		dis = BlockPos.fromLong(compound.getLong("dis"));
		rec = new ImmutablePair<BlockPos, EnumFacing>(BlockPos.fromLong(compound.getLong("rec")), EnumFacing.values()[compound.getInteger("face")]);
		current = new Vec3d(compound.getDouble("xx"), compound.getDouble("yy"), compound.getDouble("zz"));
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound c = new NBTTagCompound();
		stack.writeToNBT(c);
		compound.setTag("stack", c);
		compound.setLong("dis", dis.toLong());
		compound.setLong("rec", rec.getLeft().toLong());
		compound.setInteger("face", rec.getRight().ordinal());
		compound.setDouble("xx", current.xCoord);
		compound.setDouble("yy", current.yCoord);
		compound.setDouble("zz", current.zCoord);
		return c;
	}

	public Transfer(BlockPos dis, BlockPos rec, EnumFacing face, ItemStack stack) {
		this.dis = dis;
		this.rec = new ImmutablePair<BlockPos, EnumFacing>(rec, face);
		this.current = new Vec3d(.5, .5, .5);
		this.stack = stack;
	}

	public boolean received() {
		return current.lengthVector() > getVec().lengthVector();
	}

	public Vec3d getVec() {
		return new Vec3d(rec.getLeft().getX() - dis.getX(), rec.getLeft().getY() - dis.getY(), rec.getLeft().getZ() - dis.getZ());
	}

	public static Transfer loadFromNBT(NBTTagCompound nbt) {
		Transfer tr = new Transfer();
		tr.readFromNBT(nbt);
		return tr;
	}

}
