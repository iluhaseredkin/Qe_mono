reset 
set terminal png
set title 'q_e(H), J_e=1.0E8 cm^{-2} c^{-1}, E_0=0.34 keB, {/Symbol g= month_09'
set logscale x 10 
set key off
set xrange[10e-2:1050.0]
set yrange[0:600]
set xlabel 'lg(q_e ,cm^{-3} c^{-1})' offset 0,-1
set ylabel 'H, km' offset 0,10
set out '/Users/iluhaseredkin/IdeaProjects/Qe_mono/q(h);J_e=1.0E8;E_0=340.0;k1(N2)=1;k2(O2)=1.14;k3(O)=0.570.png'
plot '/Users/iluhaseredkin/IdeaProjects/Qe_mono/q(h);J_e=1.0E8;E_0=340.0;k1(N2)=1;k2(O2)=1.14;k3(O)=0.570' w l 
