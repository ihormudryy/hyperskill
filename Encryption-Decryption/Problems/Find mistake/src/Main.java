class Test {

    public static void main(String[] args) {
        new TeamLead(1);
    }

    public static class TeamLead extends Programmer {

        private final int numTeamLead;

        public TeamLead(int numTeamLead) {
            super(numTeamLead);
            this.numTeamLead = numTeamLead;
            //employ();
        }

        protected void employe() {
            System.out.println(numTeamLead + " teamlead");
        }

    }

    public static class Programmer {

        private final int numProgrammer;

        public Programmer(int numProgrammer) {
            this.numProgrammer = numProgrammer;
            employ();
        }

        protected void employ() {

            System.out.println(numProgrammer + " programmer");
            System.out.println(numProgrammer + " teamlead");
        }
    }
}