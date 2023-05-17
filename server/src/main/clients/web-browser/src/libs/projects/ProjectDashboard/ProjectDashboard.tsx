import './ProjectDashboard.scss';
import GaugeChart from 'react-gauge-chart';

function ProjectDashboard() {
  return (
    <section>
      <h2>Post Tracking</h2>
      <GaugeChart
        id="gauge-chart1"
        nrOfLevels={5}
        hideText
        needleBaseColor="#00000000"
        needleColor="#00000000"
        animate={false}
        colors={['#eee', '#eee', '#eee', '#f00', '#f00']}
        style={{width: 'auto'}}
      />
    </section>
  );
}

export {ProjectDashboard};
