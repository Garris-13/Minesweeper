package minesweeper;

public class Animation {
    long startTime;//动画开始时间
    long duration;//动画持续时间
    AnimationCallback callback;

    public Animation(long duration, AnimationCallback callback)
    {
        this.startTime = System.currentTimeMillis();
        this.duration = duration;
        this.callback = callback;
    }

    public boolean update()
    {
        long now = System.currentTimeMillis();//获取系统当前时间
        float t = (now - startTime)/(float)duration;//计算已经流失时间并转换为标准时间因子
        t = Math.min(1.0f,t);//限制最大值
        callback.onUpdate(t);//动画回调
        return t < 1.0f;//判断动画是否完成
    }
}
