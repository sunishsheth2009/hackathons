NBA Oracle  
==========

## Local Hack Day Fall 2014

A project dedicated to all the basketball super-fans/fantasy league all-stars out there.  

NBA Oracle retrieves live data from ESPN using Jsoup and writes team stats and schedules to .stats.nba and .scheds.nba, respectively (I recommend pulling this data weekly in order to update moving stats like winningness). It then calculates a number of statistics (DISCLAIMER: these calculations are entirely created by me and have in no way been verified or tested, so I would not recommend making any financially-backed or reputationally-important fantasy decisions using the Oracle's rankings) and gives each team an overall score. This index will soon have a cool name/abbreviation. The final product is a list of teams, ranked by score, that is written to .rank.nba.  

I am inexperienced in the world of sports math and would greatly appreciate any pointers on my ranking approach as it is currently a very simple and arbitrary system.  

## Stats Index
__winningness__: a measure of a team's current winning potential weighted primarily by schedule difficulty, the last 10 games, and expected winning  
_formula_: `((RPI + SOS) / 2) + (|EWP - WP| * (EWP + WP)) * (P / PA) + (1 - (PR / 30)) + ((W10 - L10) / 10)`  

more stats coming...  

## Stats Glossary
_RPI_: Relative Percent Index/Rating Percentage Index  
_SOS_: Strength of Schedule  
_EWP_: Estimated Winning Percentage  
_WP_: Winning Percentage  
_P_: Points  
_PA_: Points Against  
_PR_: Power Ranking (ESPN)  
_W10_: Wins (Last 10)  
_L10_: Losses (Last 10)  

__author(s)__: [Dennis](https://github.com/dchengy)
