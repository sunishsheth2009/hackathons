NBA Oracle  
==========

## Local Hack Day Fall 2014

A project dedicated to all the basketball super-fans/fantasy league all-stars out there.  

NBA Oracle retrieves live data from ESPN using Jsoup and writes team stats and schedules to .stats.nba and .scheds.nba, respectively (I recommend pulling this data weekly in order to update moving stats like winningness). It then calculates a number of statistics (DISCLAIMER: these calculations are entirely created by me and have in no way been verified or tested so I would not recommend making any financially-backed or reputationally-important fantasy decisions using the Oracle's rankings) and gives each team an overall score. This index will soon have a cool name/abbreviation. The final product is a list of teams, ranked by score, that is written to .rank.nba.  

I am inexperienced in the world of sports math and would greatly appreciate any pointers on my ranking approach as it is currently a very simple and arbitrary system.  

## Stats Glossary

__overall__: an aggregate score of a team based on the stats below  
_formula_: `(offense / 5) + (defense / 5) + (efficiency / 10) + secondary + (winningness * 10)`  

__winningness__: a measure of a team's current winning potential weighted primarily by schedule difficulty, the last 10 games, and expected winning  
_formula_: `((RPI + SOS) / 2) + (|EWP - WP| * (EWP + WP)) * (P / PA) + (1 - (PR / 30)) + ((W10 - L10) / 10)`  

__offense__: a team's scoring ability based primarily on shooting percentage  
_formula_: `((((fgp + tpfg) * 100) + ap) / 2) + (tss - efg) + ppgd + (ftp / 100)`  

__defense__: a team's defensive capability based on opponent shooting percentages as well steals and blocks  
_formula_: `((1 - ofg) * 100) + ((1 - otfg) * 100) - oppg + spg + bpg`  

__secondary__: miscellaneous stats including rebounds, turnovers, and assists  
_formula_: `(((rr + drr + orr) / 3) - 50) - (tr / 100) * (t / ot) + (ar / 100)`  

__efficiency__: overall team efficiency calculated by Hollinger stats  
_formula_: `(pf / 100) * (oe - de)  

more stats coming...  

## Key
_RPI_: Relative Percent Index/Rating Percentage Index  
_SOS_: Strength of Schedule  
_EWP_: Estimated Winning Percentage  
_WP_: Winning Percentage  
_P_: Points  
_PA_: Points Against  
_PR_: Power Ranking (ESPN)  
_W10_: Wins (Last 10)  
_L10_: Losses (Last 10)  
_FGP_: Field Goal Percentage  
_TPFG_: 3-Point Field Goal Percentage  
_AP_: Average Points For  
_TSS_: True Shooting Percentage  
_EFG_: Effective Shooting Percentage  
_PPGD_: Points Per Game Differential  
_FTP_: Free Throw Percentage  
_OFG_: Opponent Field Goal Percentage  
_OTFG_: Opponent 3-Point Field Goal Percentage  
_OPPG_: Opponent Points Per Game  
_SPG_: Steal Per Game  
_BPG_: Blocks Per Game  
_RR_: Rebound Rate  
_DRR_:  Defensive Rebound Rate  
_ORR_: Offensive Rebound Rate  
_TR_: Turnover Ratio  
_T_: Turnovers Per Game  
_OT_: Opponent Turnovers Per Game  
_AR_: Assist Ratio  
_PF_: Pace Factor  
_OE_: Offensive Efficiency  
_DE_: Defensive Efficiency  

__author(s)__: [Dennis](https://github.com/dchengy)
