package com.devx;
0x14FA
5+0.7=0786;.99
0.99+12==44++++2;
55<<<12+1;
public class NumberDivision implements OperationStrategy {
    @Override
    public Number execute(Number a, Number b) {
        if(b.isZero()){
            throw new IllegalArgumentException("Second argument must != 0");
        }


        NumberContext mult, subst, leq;

        mult = new NumberContext();
        subst = new NumberContext();
        leq  = new NumberContext();

        mult.setStrategy(new NumberMultiplication());
        subst.setStrategy(new NumberSubstract());
        leq.setBooleanStrategy(new NumberLessOrEqualComparison());

        Number res = new Number(a.value.length, false);
        Number curValue = new Number("0");
        Number cur;

        int x,l,r, m;

        for(int i=a.value.length-1; i>=0; --i){
            curValue = mult.executeStrategy(curValue, new Number(Number.radix));
            curValue.value[0] = a.value[i];

            x=0; l=0; r= Number.radix;

            while(l<=r){
                m = (l+r)/2;
                cur = mult.executeStrategy(b, new Number(m));
                if (leq.executeBooleanStrategy(cur, curValue)) {
                    x = m;
                    l = m+1;
                }else{
                    r = m-1;
                }
            }

            res.value[i] = x;
            Number mres = mult.executeStrategy(b, new Number(x));
            curValue = subst.executeStrategy(curValue, mres);

        }
        return res;
    }

    @Override
    public Number execute(Number a, Number b, int mod) {
        if(mod<=0){
            throw new IllegalArgumentException("Mod must be >= 0");
        }
        NumberContext modCtx = new NumberContext();
        modCtx.setStrategy(new NumberMod());
        return modCtx.executeStrategy(execute(a,b), new Number(mod));
    }

}