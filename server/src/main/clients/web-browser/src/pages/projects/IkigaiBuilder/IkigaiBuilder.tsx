import '../../DefaultPageNav.scss';
import './IkigaiBuilder.scss';
import {Layout} from 'antd';
import {Coordinate, Ikigai} from '../../../Ikigai/Ikigai';
import {useMeasure} from '@react-hookz/web';
import {useEffect, useState} from 'react';
const {Sider, Content} = Layout;

export function IkigaiBuilder() {
  const [ikigaiCenterPosition, setIkigaiCenterPosition] =
    useState<Coordinate | null>(null);
  const [ikigaiDistanceToCategoryCenter, setIkigaiDistanceToCategoryCenter] =
    useState(0);
  const [ikigaiCategoryDiameter, setIkigaiCategoryDiameter] = useState(0);

  const [ikigaiContainerMeasure, ikigaiContainerMeasureRef] =
    useMeasure<HTMLDivElement>();

  const [useikigaiResizeTimeout, setUseikigaiResizeTimeout] = useState(true);
  const [ikigaiResizeTimeoutId, setIkigaiResizeTimeoutId] = useState<
    NodeJS.Timeout | undefined
  >(undefined);

  // Resize and reposition the Ikigai diagram to be consistent with the window.
  function updateIkigaiPosition() {
    if (
      ikigaiContainerMeasureRef.current != null &&
      ikigaiContainerMeasure != null
    ) {
      setIkigaiCenterPosition({
        x:
          ikigaiContainerMeasureRef.current.offsetLeft +
          ikigaiContainerMeasure.width / 2,
        y:
          ikigaiContainerMeasureRef.current.offsetTop +
          ikigaiContainerMeasure.height / 2,
      });
      setIkigaiCategoryDiameter(
        Math.min(ikigaiContainerMeasure.width, ikigaiContainerMeasure.height) /
          2
      );
      setIkigaiDistanceToCategoryCenter(
        (Math.min(ikigaiContainerMeasure.width, ikigaiContainerMeasure.height) /
          2) *
          0.45
      );
    }
    setUseikigaiResizeTimeout(false);
  }

  useEffect(() => {
    // TODO: This is a terrible solution. Revisit.
    if (useikigaiResizeTimeout) {
      if (ikigaiResizeTimeoutId != null) {
        clearTimeout(ikigaiResizeTimeoutId!);
      }
      setIkigaiResizeTimeoutId(setTimeout(updateIkigaiPosition, 500));
    } else {
      updateIkigaiPosition();
    }
  }, [ikigaiContainerMeasureRef, ikigaiContainerMeasure]);

  return (
    <>
      <Layout style={{height: '100%'}}>
        <Content style={{borderRight: '#F0781F solid 1px'}}>
          <div
            style={{width: '100%', height: '100%'}}
            ref={ikigaiContainerMeasureRef}
          >
            <div className="subtitle">Ikigai Builder</div>
            <div className="brief-instructions">Click each circle to edit.</div>
            <Ikigai
              id="ikigai-builder"
              centerPosition={ikigaiCenterPosition}
              categoryDiameter={ikigaiCategoryDiameter}
              distanceToCategoryCenter={ikigaiDistanceToCategoryCenter}
              lovesResizeAndRotateElement={
                <>
                  Something you <b>LOVE</b>
                </>
              }
              worldNeedsResizeAndRotateElement={
                <>
                  What the world <b>NEEDS</b>
                </>
              }
              paidForResizeAndRotateElement={
                <>
                  What you can be <b>PAID&nbsp;FOR</b>
                </>
              }
              goodAtResizeAndRotateElement={
                <>
                  What you are <b>GOOD&nbsp;AT</b>
                </>
              }
            />
          </div>
        </Content>
        <Sider reverseArrow>
          <div>Saved Projects</div>
        </Sider>
      </Layout>
    </>
  );
}
