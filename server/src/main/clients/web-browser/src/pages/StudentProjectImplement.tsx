import { useSearchParams } from "react-router-dom";
import ProfileCard from "./ProfileCard";

function App() {
    const [searchParams,] = useSearchParams();

    return (<main>
        <h1>Project Implementation</h1>
        <div className="header-sidebar-layout">
            <div className="layout-header">
                <h2>Project Idea</h2>
                <p>{searchParams.get('description')}</p>
            </div>
            <div>
                <h2>Implementation Steps</h2>
                <ol>
                    <li>Lorem</li>
                    <li>Ipsum</li>
                    <li>Dolor</li>
                    <li>Sit</li>
                    <li>Amet</li>
                    <li>Orange</li>
                    <li>Pufferfish</li>
                </ol>
            </div>
            <h2>Professional Connections</h2>
            <div className="tiles">
                <ProfileCard title="John Doe" imageSrc="" description="A doe" />
                <ProfileCard title="John Doe" imageSrc="" description="A doe" />
                <ProfileCard title="John Doe" imageSrc="" description="A doe" />
            </div>
        </div>
    </main>);
}

export default App;
